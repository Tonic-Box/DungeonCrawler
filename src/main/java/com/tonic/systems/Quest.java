package com.tonic.systems;

public class Quest {
    public enum QuestType {
        DEFEAT, COLLECT, EXPLORE
    }

    public String title;
    public String description;
    public QuestType type;
    public int target; // e.g., number of monsters to defeat.
    public int progress;
    public boolean completed;

    public Quest(String title, String description, QuestType type, int target) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.target = target;
        this.progress = 0;
        this.completed = false;
    }

    public void updateProgress(int amount) {
        if (!completed) {
            progress += amount;
            if (progress >= target) {
                progress = target;
                completed = true;
                System.out.println("Quest completed: " + title);
            }
        }
    }
}
