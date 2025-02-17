package com.tonic.systems;

import java.util.ArrayList;
import java.util.List;

public class QuestManager {
    private List<Quest> quests;

    public QuestManager() {
        quests = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void updateProgress(int amount) {
        // For simplicity, update all DEFEAT-type quests.
        for (Quest quest : quests) {
            if (quest.type == Quest.QuestType.DEFEAT && !quest.completed) {
                quest.updateProgress(amount);
            }
        }
    }

    public void update() {
        // Additional quest logic can be added here.
    }
}
