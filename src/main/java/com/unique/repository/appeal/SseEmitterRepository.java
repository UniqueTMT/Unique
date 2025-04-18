package com.unique.repository.appeal;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long userSeq, SseEmitter emitter) {
        emitters.put(userSeq, emitter);
    }

    public void removeEmitter(Long userSeq) {
        emitters.remove(userSeq);
    }

    public SseEmitter getEmitter(Long userSeq) {
        return emitters.get(userSeq);
    }
}

