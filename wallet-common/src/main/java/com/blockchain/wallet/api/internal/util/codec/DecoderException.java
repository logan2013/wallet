package com.blockchain.wallet.api.internal.util.codec;


public class DecoderException extends Exception {

    private static final long serialVersionUID = 5007039195320544167L;

    /**
     * Creates a DecoderException
     * 
     * @param pMessage A message with meaning to a human
     */
    public DecoderException(String pMessage) {
        super(pMessage);
    }

}  

