package cl.dressed.bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"SERVER_PORT=8080",
		"BACKEND_URL=http://localhost:8090",
		"JWT_SECRET=cambia-esto-por-un-secreto-seguro-de-minimo-32-caracteres",
		"JWT_COOKIE_NAME=access_token",
		"JWT_COOKIE_MAX_AGE=86400",
		"CORS_ALLOWED_ORIGINS=http://localhost:5173"
})
class BffApplicationTests {

	@Test
	void contextLoads() {
	}

}
