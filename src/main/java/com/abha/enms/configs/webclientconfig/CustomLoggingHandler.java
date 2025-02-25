package com.abha.enms.configs.webclientconfig;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom Netty handler for logging incoming messages and handling exceptions.
 */
@Slf4j
public class CustomLoggingHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof ByteBuf byteBuf) {
      String message = byteBuf.toString(CharsetUtil.UTF_8);
      // Log the incoming message
      log.info("Received message: {}", message);
      // Pass the message to the next handler in the pipeline
      ctx.fireChannelRead(msg);
    }
    // Pass the message to the next handler in the pipeline
    ctx.fireChannelRead(msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    // Log when the channel read is complete
//    log.info("Channel read complete");
    // Pass the event to the next handler in the pipeline
    ctx.fireChannelReadComplete();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Log exceptions
    log.error("Exception caught: {}", cause.getMessage());
    // Close the connection on exception
    ctx.close();
  }
}
