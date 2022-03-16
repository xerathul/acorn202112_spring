package com.gura.spring03.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Component
public class AuthAspect {

	@Around("execution(org.springframework.web.servlet.ModelAndView auth*(..))")
	public Object loginCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args=joinPoint.getArgs();
		//메소드에 전달된 인자중에서 HttpServletRequest 객체를 찾는다.
		for(Object tmp:args) {
			if(tmp instanceof HttpServletRequest) {
				//찾았으면 원래 type 으로 casting
				HttpServletRequest request=(HttpServletRequest)tmp;
				//HttpSession 객체의 참조값 얻어와서 로그인 여부를 알아낸다.
				String id=(String)request.getSession().getAttribute("id");
				if(id == null) {//만일 로그인을 하지 않았으면
					//로그인 페이지로 리다일렉트 할수 있는 ModelAndView 객체를 생성해서 
					ModelAndView mView=new ModelAndView();
					mView.setViewName("redirect:/users/loginform.do");
					//메소드를 여기서 리턴 시킨다. 
					return mView;
				}
			}
		}

		//로그인을 했으면 아래의 코드가 수행되고 ModelAndView 객체가 Object type 으로 리턴된다. 
		Object obj=joinPoint.proceed();

		return obj;
	}
}

