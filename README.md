**一、最简单的spring security程序**
=========================

*1、引入spring-boot-starter-security依赖*
------------------

	<dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-security</artifactId>
	</dependency>

*2、简单的测试controller*
--------------------

	@RestController
	public class ExampleController {

    	@GetMapping("helloworld")
    	public List<String> helloworld() {
       	 return Arrays.asList("Spring Security simple demo");
   	 }
	}
*3. 开始验证*
------------------

启动应用程序，控制台会打印一个生成的密码，如"Using generated security password: 4dd8384a-bc9e-4df0-9124-6686c9a813fa"，该密码每次启动应用程序都会改变
访问 (http://localhost:8081/helloworld)
系统会自动重定向到 (http://localhost:8081/login) （注意：这个登录页面不是自己写的，是Spring Security默认的登录页面）
输入用户名和密码，用户名为"user", 密码就是控制台生成的"4dd8384a-bc9e-4df0-9124-6686c9a813fa"
系统会自动重定向到 (http://localhost:8081/helloworld) ，从而能够访问到接口

**三：示例分析**
=================================

可以看到上面集成Spring Security非常简单(虽然这只是雏形)，示例是一个认证过程(也就是登录功能)，要学会Spring Security个人觉得非常有必要看一下源码是如何实现的，下面就简单的分析一下整个认证的过程, 我们可以根据控制台输出的日志来窥探整个认证执行的流程。

下面是Spring Security认证的重要流程，自己可以打断点看一下程序是怎么执行的。

*过程一：从访问的目标接口重定向到[登录页面](http://localhost:8080/helloworld)*
----------------------
	| AnonymousAuthenticationFilter#doFilter	检查安全上下文SecurityContextHolder中是否有认证信息，如果没有就设置为匿名认证令牌	AnonymousAuthenticationToken
	| FilterSecurityInterceptor extends AbstractSecurityInterceptor#doFilter
		| FilterSecurityInterceptor#invoke
			| AbstractSecurityInterceptor#beforeInvocation
			 	| AffirmativeBased extends AbstractAccessDecisionManager#decide 访问决定管理器: 决定一个url是否有权限访问，具体决定操作由投票器决定
					| WebExpressionVoter#vote() 投票器: 对url是否有权限访问进行投票，是否允许访问，允许则投"通过"，不允许则投"拒绝"
						| ExpressionUtils#evaluateAsBoolean
							|SpelExpression#getValue(org.springframework.expression.EvaluationContext, java.lang.Class<T>)
								| PropertyOrFieldReference#getValueInternal()
									| PropertyOrFieldReference#readProperty
										| ReflectivePropertyAccessor.OptimalPropertyAccessor#read
											| SecurityExpressionRoot#isAuthenticated() 投票的最终结果(拒绝)
			 	| 如果投票结果是拒绝则抛出访问拒绝异常new AccessDeniedException("Access is denied")
	| ExceptionTranslationFilter#doFilter 异常转换过滤器：用于捕获过滤器抛出的异常，并作出适当的处理
		| catch(Exception ex)
		| handleSpringSecurityException(request, response, chain, ase)
			| sendStartAuthentication()	
				| DelegatingAuthenticationEntryPoint#commence
					| LoginUrlAuthenticationEntryPoint#commence
						| DefaultRedirectStrategy#sendRedirect(request, response, redirectUrl); 重定向登录路径redirectUrl="http://localhost:8081/login"
							| response.sendRedirect(redirectUrl)							
	| DefaultLoginPageGeneratingFilter#doFilter 拦截登录路径"/login", 如果没有指定登录页面就会生成默认的登录页面
	| generateLoginPageHtml()
	| response.setContentType("text/html;charset=UTF-8")
	| response.getWriter().write(loginPageHtml)

*过程二：从登录页面重定向到目标接口*
---------------------

	| 输入用户名、密码登录	
		| UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter#doFilter
	| Authentication authResult = attemptAuthentication(request, response) 尝试认证
		| authRequest = new UsernamePasswordAuthenticationToken(username, password)
		| ProviderManager.authenticate(authRequest)
			| DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider#authenticate
				| retrieveUser(username, authentication) 
					| UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username)
						| InMemoryUserDetailsManager#loadUserByUsername 如果用户名错误会抛异常 UsernameNotFoundException
				| additionalAuthenticationChecks(user, authentication)
					| passwordEncoder.matches(presentedPassword, userDetails.getPassword()) 检查密码，如果密码不匹配则抛出异常BadCredentialsException
	| successfulAuthentication(request, response, chain, authResult) 处理认证成功操作
		| SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess
			| SimpleUrlAuthenticationSuccessHandler#onAuthenticationSuccess
				| handle(request, response, authentication)
					| DefaultRedirectStrategy#sendRedirect(request, response, targetUrl) 
						| response.sendRedirect(redirectUrl) 重定向到"http://localhost:8080/helloworld"

首先我们要知道Spring Security的基本原理就是用一堆过滤器来实现的，就是一个请求过来会经过很多个过滤器的拦截，如果所有过滤器都通过就能访问，如果不满足条件就抛异常，终止访问。

过程一源码分析
启动应用程序，[访问接口](http://localhost:8080/helloworld)
”/helloworld“ 路径首先会被AnonymousAuthenticationFilter进行拦截，该拦截器会检查认证上下文SecurityContextHolder中是否有认证信息，如果没有就给一个匿名认证信息
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			Authentication authentication = createAuthentication((HttpServletRequest) req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		}
	}

	protected Authentication createAuthentication(HttpServletRequest request) {
		AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken("12676a06-df4a-475b-bb7c-4d3ec4bd1c9b",
				"anonymousUser", Arrays.asList("ROLE_ANONYMOUS"));
		auth.setDetails(authenticationDetailsSource.buildDetails(request));
		return auth;
	}
FilterSecurityInterceptor是Spring Security过滤器链中的最后一个过滤器，负责来决定请求是否最终有权限来访问。在该过滤器方法调用中链中AbstractAccessDecisionManager#decide和WebExpressionVoter#vote是需要注意的两个方法，WebExpressionVoter是一种投票器，可以对访问的url进行投票，可以投"通过"，也可以投"拒绝"。 SecurityExpressionRoot#isAuthenticated()方法会返回最终的投票的结果。Spring Security默认所有的请求都需要登录认证，因我们访问"/helloworld"接口没有登录，所以投票器会投"拒绝"票(AccessDecisionVoter.ACCESS_DENIED)

	public class AffirmativeBased extends AbstractAccessDecisionManager {
		public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws 		AccessDeniedException {
		int deny = 0;

		for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, object, configAttributes);

			if (logger.isDebugEnabled()) {
				logger.debug("Voter: " + voter + ", returned: " + result);
			}

			switch (result) {
				case AccessDecisionVoter.ACCESS_GRANTED:
					return;
	
				case AccessDecisionVoter.ACCESS_DENIED:
					deny++;
	
					break;
	
				default:
					break;
			}
		}

		// 如果有"拒绝"票，则抛出访问拒绝异常
		if (deny > 0) {
			throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
		}

		checkAllowIfAllAbstainDecisions();
	}
	}
ExceptionTranslationFilter是倒数第二个过滤器，它会捕获FilterSecurityInterceptor抛出的异常并对异常进行逻辑处理。如果访问拒绝(认证失败)就会重定向到登录地址"/login"

	public class ExceptionTranslationFilter extends GenericFilterBean {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception ex) {
			handleSpringSecurityException(request, response, chain, ase);
		}

	}
	
	private void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response, FilterChain chain, RuntimeException exception)throws IOException, ServletException {
		if (exception instanceof AccessDeniedException) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authenticationTrustResolver.isAnonymous(authentication) || authenticationTrustResolver.isRememberMe(authentication)) {
				sendStartAuthentication(request, response, chain, new InsufficientAuthenticationException(messages.getMessage("ExceptionTranslationFilter.insufficientAuthentication", "Full authentication is required to access this resource")));
			}
	}
	}
	public class DefaultRedirectStrategy implements RedirectStrategy {
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response,
			String url) throws IOException {
		String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
		// http://localhost:8080/login
		redirectUrl = response.encodeRedirectURL(redirectUrl);

		if (logger.isDebugEnabled()) {
			logger.debug("Redirecting to '" + redirectUrl + "'");
		}

		response.sendRedirect(redirectUrl);
	}
	}
当系统访问"/login"路径时会被默认的登录页面生成过滤器DefaultLoginPageGeneratingFilter所拦截，系统会判断自己有没有指定登录页面，如果没有指定系统就会生成一个默认的登录页面

	public class DefaultLoginPageGeneratingFilter extends GenericFilterBean {
	public static final String DEFAULT_LOGIN_PAGE_URL = "/login";
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		boolean loginError = isErrorPage(request);
		boolean logoutSuccess = isLogoutSuccess(request);
		if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
			// 生成登录页面
			String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
			response.setContentType("text/html;charset=UTF-8");
			response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(loginPageHtml);

			return;
		}

		chain.doFilter(request, response);
	}
	}	

*过程二源码分析*
---------------------
>用户在登录页面输入用户名和密码点击登录
登录时被用户名密码认证过滤器UsernamePasswordAuthenticationFilter所拦截，去校验用户名和密码是否正确。检查用户名是在DaoAuthenticationProvider#retrieveUser(username, authentication) 方法中检查，检查密码是在DaoAuthenticationProvider#additionalAuthenticationChecks(user, authentication)中检查。如果用户名和密码都是正确的，则重定向到上次访问的路径上，即我们第一次访问的(http://localhost:8080/helloworld)路径上。

	public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		prepareTimingAttackProtection();
		try {
			UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"UserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (InternalAuthenticationServiceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}
	
	
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();
		
		// 检查密码是否正确
		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
	}
	}
	public class InMemoryUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = users.get(username.toLowerCase());

		// 检查用户名是否正确
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
				user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isAccountNonLocked(), user.getAuthorities());
	}
	}		
**四：Spring Security 默认的配置**
================================

Spring Security中可以通过配置来配置一些参数，比如哪些路径需要认证，登录页面相关的配置(如登录的路径、登录成功时要跳转的路径、登录成功时的处理器、登录失败时要跳转的路径、登录失败时的处理器、登出的路径等)、在过滤器链中添加自己的过滤器(addFilterBefore)等，可以配置很多。如果没有显式配置Spring Security会提供一套默认的值，默认的配置大致如下配置：

	@Configuration
	@EnableWebSecurity
	public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

   	 @Override
    	protected void configure(HttpSecurity http) throws Exception {
       	 http.csrf().disable()
                	// 配置需要认证的请求
                	.authorizeRequests()
                    	.anyRequest()
                    	.authenticated()
                    	.and()
               	 	// 登录表单相关配置
                	.formLogin()
                    	.usernameParameter("username")
                    	.passwordParameter("password")
                    	.failureUrl("/login?error")
                    	.permitAll()
                    	.and()
                	// 登出相关配置
                	.logout()
                    	.permitAll();

   	 }
	}
**五: Spring Security过滤器链**
=============================

Spring Security主要用于认证Authentication(登录)和授权Authorize(api是否有权访问)，实现这些功能的基本原理就是过滤器链，即当访问一个url时会被过滤器链中的每个过滤器所拦截，如果每个过滤器都没有抛异常则表示当前用户允许访问该url，则重定向到用户需要访问的url上，如果有一个过滤器抛出异常了则表示当前用户没有权限访问该url，此时可以报错。

Spring Security使用到的过滤器：

	WebAsyncManagerIntegrationFilter
	SecurityContextPersistenceFilter
	HeaderWriterFilter
	CsrfFilter
	LogoutFilter
	BasicAuthenticationFilter
	UsernamePasswordAuthenticationFilter
	RememberMeAuthenticationFilter
	SocialAuthenticationFilter
	DefaultLoginPageGeneratingFilter
	DefaultLogoutPageGeneratingFilter
	RequestCacheAwareFilter
	SecurityContextHolderAwareRequestFilter
	SessionManagementFilter
	AnonymousAuthenticationFilter
	ExceptionTranslationFilter
	FilterSecurityInterceptor
