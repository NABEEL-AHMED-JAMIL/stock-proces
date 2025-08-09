package com.stock.process.api;

import com.stock.process.ai.HelloChatAIService;
import com.stock.process.ai.dto.InputAIPrompt;
import com.stock.process.ai.dto.OutputAIPrompt;
import com.stock.process.domain.dto.AppResponse;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/ai.json")
public class HelloChatAIRestApi {

    private Logger logger = LoggerFactory.getLogger(HelloChatAIRestApi.class);

    private final HelloChatAIService helloChatAIService;

    public HelloChatAIRestApi(HelloChatAIService helloChatAIService) {
        this.helloChatAIService = helloChatAIService;
    }

    /**
     * @apiName :- chat
     * api use to chat with ollama
     * @return ResponseEntity<?>
     * */
    @PostMapping(value="/chat")
    public ResponseEntity<?> chat(@RequestBody InputAIPrompt aiPrompt) {
        logger.info("HelloChatAIRestApi :: chat -> call fileId={} ", aiPrompt);
        try {
            OutputAIPrompt outputAIPrompt = this.helloChatAIService.chat(aiPrompt);
            return new ResponseEntity<>(new AppResponse(BarcoUtil.SUCCESS, "Chat response successfully.", outputAIPrompt), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while chat ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
