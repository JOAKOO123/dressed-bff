package cl.dressed.bff.service;

import cl.dressed.bff.client.AdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminClient adminClient;

    public Map<String, Object> getMetrics(String token) {
        log.info("Obteniendo métricas de admin");
        return adminClient.getMetrics(token);
    }

    public Map<String, Object> getUsers(String token, int page, int size) {
        log.info("Obteniendo usuarios admin - page={} size={}", page, size);
        return adminClient.getUsers(token, page, size);
    }
}
