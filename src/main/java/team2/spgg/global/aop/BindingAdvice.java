package team2.spgg.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.responseDto.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * AOP (Aspect-Oriented Programming) 관점 지향 프로그래밍을 사용하여 컨트롤러 메서드의 파라미터에 대한 유효성 검사를 수행하는 클래스입니다.
 * 파라미터에 대한 유효성 검사를 위해 BindingResult를 사용하여 에러 정보를 처리하고, ApiResponse를 반환하여 요청에 대한 응답을 처리합니다.
 */
@Component
@Aspect
public class BindingAdvice {

	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);

	@Around("execution(* team2.spgg.domain.*Controller.*(..))")
	public Object validationHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();

		System.out.println("type : " + type);
		System.out.println("method : " + method);

		Object[] args = proceedingJoinPoint.getArgs();

		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;

				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError error : bindingResult.getFieldErrors()) {
						log.warn(type + method + error.getDefaultMessage());
						errorMap.put(error.getField(), error.getDefaultMessage());
					}

					ErrorResponse errorResponse = new ErrorResponse("요청에 실패하였습니다.", HttpStatus.BAD_REQUEST);
					return ApiResponse.error(errorResponse);
				}
			}
		}

		System.out.println("BindingAdvice : 종료 ===========================================");
		System.out.println();
		return proceedingJoinPoint.proceed();
	}
}
