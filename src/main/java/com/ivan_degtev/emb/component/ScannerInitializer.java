package com.ivan_degtev.emb.component;

import com.ivan_degtev.emb.service.ScannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScannerInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ScannerService scannerService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("DataInitializer started");

        scannerService.scanner();
    }


}
