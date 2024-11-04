package temp_server.domain.temp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import temp_server.domain.temp.service.TempService;
import temp_server.global.common.Response;

@RestController
@RequestMapping("/api/v1/temps")
@RequiredArgsConstructor
public class TempController {
    private final TempService tempService;

    @PostMapping("")
    public ResponseEntity<Response> saveTemp() {

        return ResponseEntity.ok(new Response("Temp 저장 완료"));
    }
}
