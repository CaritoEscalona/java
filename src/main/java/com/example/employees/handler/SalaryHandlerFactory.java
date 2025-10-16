package com.example.employees.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SalaryHandlerFactory {
    private final Map<String, SalaryHandler> handlers = new HashMap<>();

    public SalaryHandlerFactory(List<SalaryHandler> list) {
        for (SalaryHandler h : list) {
            handlers.put(h.getRoleName(), h);
        }
    }

    public SalaryHandler getHandler(String roleName) {
        return handlers.get(roleName.toUpperCase());
    }
}
