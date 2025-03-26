package me.cerdax.cerkour.utils.protocol.sign;

@FunctionalInterface
public interface SignCompleteHandler {

    void onSignClose(UpdateSignEvent event);

}