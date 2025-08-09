package com.stock.process.ai;

import com.stock.process.ai.dto.InputAIPrompt;
import com.stock.process.ai.dto.OutputAIPrompt;
import dev.langchain4j.model.chat.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Ahmed
 */
@Service
public class HelloChatAIService {

    private Logger logger = LoggerFactory.getLogger(HelloChatAIService.class);

    private final ChatModel chatModel;

    public HelloChatAIService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public OutputAIPrompt chat(InputAIPrompt aiPrompt) {
        return new OutputAIPrompt().setText(this.chatModel.chat(aiPrompt.getUserMessage()));
    }

}
