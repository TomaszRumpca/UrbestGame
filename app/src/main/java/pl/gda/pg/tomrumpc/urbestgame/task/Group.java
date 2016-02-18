package pl.gda.pg.tomrumpc.urbestgame.task;

/**
 * Created by torumpca on 2016-01-05.
 */
public class Group {

    private int groupId;
    private String groupName;
    private String color;

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getColor() {
        return color;
    }

    public Group(Builder builder) {
        this.groupId = builder.groupId;
        this.groupName = builder.groupName;
        this.color = builder.color;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int groupId;
        private String groupName;
        private String color;

        private Builder() {
        }

        public Group build() {
            return new Group(this);
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
