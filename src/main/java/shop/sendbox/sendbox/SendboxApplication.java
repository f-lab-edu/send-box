package shop.sendbox.sendbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/*
SpringBootApplication 애노테이션 설명
1. 스프링 부트가 제공하는 기본 설정된 빈을 등록하는 애노테이션이 포함되어있습니다.
2. 해당 어노테이션과 같은 위치에 아래에 있는 @Component,@Configuration 클래스를 빈으로 등록합니다.
3. 해당 어노테이션을 가진 클래스가 스프링 부트에 필요한 설정 클래스임을 명시합니다.
 */
@SpringBootApplication
public class SendboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendboxApplication.class, args);
	}

}
