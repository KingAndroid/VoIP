
package EventHandlers;

import UDP.ConversationExecutorUDP;
import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
/**
 *
 * @Author : Ofir Attia
 * @File : MuteIncomingCheckboxHandler.java
 * @Description : The following file is a custom EventListener of the checkbox
 * in settings GUI. When the user check/uncheck the checkbox, it will execute
 * method of conversation object and user will be able to choose to hear the
 * voice mates or not.
 * @Date : 9/3/2015
 */
public class MuteIncomingCheckboxHandler implements Runnable {

    private ConversationExecutorUDP CO;
    private final JCheckBox muteCheckBox;

    /**
     *
     * @param ConversationObject
     * @param muteCheckBox
     */
    public MuteIncomingCheckboxHandler(ConversationExecutorUDP ConversationObject, JCheckBox muteCheckBox) {
        this.CO = ConversationObject;
        this.muteCheckBox = muteCheckBox;
    }

    /**
     * update the Conversation Object
     *
     * @param newCO
     */
    public void setCO(ConversationExecutorUDP newCO) {
        this.CO = newCO;
    }

    @Override
    public synchronized void run() {
        this.muteCheckBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                CO.muteInComing(); 

            } else {
                CO.unmuteInComing();
            }
        });

    }

}
