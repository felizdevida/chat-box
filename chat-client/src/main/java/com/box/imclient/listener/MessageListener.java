package com.box.imclient.listener;


import com.box.imcommon.model.SendResult;

public interface MessageListener {

     void process(SendResult result);

}
