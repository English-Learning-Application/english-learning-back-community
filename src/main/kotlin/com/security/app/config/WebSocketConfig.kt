package com.security.app.config

import com.security.app.utils.JwtTokenUtils
import com.sun.security.auth.UserPrincipal
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val jwtTokenUtils: JwtTokenUtils,
) : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*")
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic", "/queue", "/user") // Real-time updates
        registry.setApplicationDestinationPrefixes("/app")
        registry.setUserDestinationPrefix("/user")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object: ChannelInterceptor{
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor = StompHeaderAccessor.wrap(message)
                val token = accessor.getFirstNativeHeader("Authorization")

                if (token != null && token.startsWith("Bearer ")) {
                    val jwtToken = token.substring(7)
                    val userId = jwtTokenUtils.getUserId(jwtToken)
                    accessor.user = UserPrincipal(userId)
                }

                return message
            }
        })
    }
}