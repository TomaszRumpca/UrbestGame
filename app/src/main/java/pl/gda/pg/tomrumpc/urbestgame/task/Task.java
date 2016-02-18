package pl.gda.pg.tomrumpc.urbestgame.task;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import static pl.gda.pg.tomrumpc.urbestgame.task.Task.State.*;

public class Task implements Serializable {

    private static final long serialVersionUID = 0L;

    public enum State {
        LOCKED(0),
        ACTIVE(1),
        DONE(2),
        WAITING_FOR_APPROVAL(3);

        Integer choice;

        State(Integer i) {
            this.choice = i;
        }

        public Integer getValue() {
            return this.choice;
        }
    }

    public enum Type {
        QA(0);

        Integer choice;

        Type(Integer i) {
            this.choice = i;
        }

        public Integer getValue() {
            return this.choice;
        }
    }

    private int taskId;
    private String taskName;
    private String taskDescription;
    private int maxPoints;
    private int achivedPoints;
    private int usedPrompts;
    private Type taskType;
    private double latitude;
    private double longitude;
    private State state;
    private String dateOfActivation;
    private String dateOfCompletion;
    private int groupId;
    private String groupName;
    private String color;

    private Task(Builder builder) {
        taskId = builder.taskId;
        taskName = builder.taskName;
        taskDescription = builder.taskDescription;
        maxPoints = builder.maxPoints;
        achivedPoints = builder.achivedPoints;
        usedPrompts = builder.usedPrompts;
        taskType = builder.taskType;
        latitude = builder.latitude;
        longitude = builder.longitude;
        state = builder.state;
        dateOfActivation = builder.dateOfActivation;
        dateOfCompletion = builder.dateOfCompletion;
        groupId = builder.groupId;
        groupName = builder.groupName;
        color = builder.color;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getAchivedPoints() {
        return achivedPoints;
    }

    public int getUsedPrompts() {
        return usedPrompts;
    }

    public Type getTaskType() {
        return taskType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public State getState() {
        return state;
    }

    public String getDateOfActivation() {
        return dateOfActivation;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getColor() {
        return color;
    }

    public boolean isActive() {
        switch (this.state) {
            case ACTIVE:
                return true;
            default:
                return false;
        }
    }

    public LatLng getLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int taskId;
        private String taskName;
        private String taskDescription;
        private int maxPoints;
        private int achivedPoints;
        private int usedPrompts;
        private Type taskType;
        private double latitude;
        private double longitude;
        private State state;
        private String dateOfActivation;
        private String dateOfCompletion;
        private int groupId;
        private String groupName;
        private String color;

        private Builder() {

        }

        public Task build() {
            return new Task(this);
        }

        public Builder taskId(int taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public Builder taskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
            return this;
        }

        public Builder maxPoints(int maxPoints) {
            this.maxPoints = maxPoints;
            return this;
        }

        public Builder achivedPoints(int achivedPoints) {
            this.achivedPoints = achivedPoints;
            return this;
        }

        public Builder usedPrompts(int usedPrompts) {
            this.usedPrompts = usedPrompts;
            return this;
        }

        public Builder taskType(Integer taskType) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(taskType)) {
                    this.taskType = type;
                }
            }
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder state(Integer stateId) {
            for (State state : State.values()) {
                if (state.getValue().equals(stateId)) {
                    this.state = state;
                }
            }
            return this;
        }

        public Builder dateOfActivation(String dateOfActivation) {
            this.dateOfActivation = dateOfActivation;
            return this;
        }

        public Builder dateOfCompletion(String dateOfCompletion) {
            this.dateOfCompletion = dateOfCompletion;
            return this;
        }

        public Builder groupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }
    }
}