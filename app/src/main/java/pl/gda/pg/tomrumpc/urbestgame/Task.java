package pl.gda.pg.tomrumpc.urbestgame;

import com.google.android.gms.maps.model.LatLng;

public class Task {

    public enum State {
        LOCKED(0),
        ACTIVE(1),
        DONE(2);

        int choice;
        State(int i){
            this.choice = i;
        }

        public int getValue(){
            return this.choice;
        }
    }
    public int id;
    public String taskName;
    public int maxPoints;
    public int achivedPoints;
    public int usedPrompts;
    public int group;
    public String LatLng;
    public int isActive;
    public String dateOfActivation;
    public String dateOfCompletion;

    public String color;

    public Task() {
    }

    public Task(int id, String taskName, int maxPoints, int achivePoints, int usedPrompts, int group, String LatLng, int isActive, String dateOfActivation, String dateOfCompletion) {
        this.id = id;
        this.taskName = taskName;
        this.maxPoints = maxPoints;
        this.achivedPoints = achivePoints;
        this.usedPrompts = usedPrompts;
        this.group = group;
        this.LatLng = LatLng;
        this.isActive = isActive;
        this.dateOfActivation = dateOfActivation;
        this.dateOfCompletion = dateOfCompletion;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getAchivedPoints() {
        return achivedPoints;
    }

    public void setAchivedPoints(int achivedPoints) {
        this.achivedPoints = achivedPoints;
    }

    public int getUsedPrompts() {
        return usedPrompts;
    }

    public void setUsedPrompts(int usedPrompts) {
        this.usedPrompts = usedPrompts;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getDateOfActivation() {
        return dateOfActivation;
    }

    public void setDateOfActivation(String dateOfActivation) {
        this.dateOfActivation = dateOfActivation;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int state;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LatLng getLatLng() {

        String[] latlng = this.LatLng.split(":");

        try {
            double latitude = Double.parseDouble(latlng[0]);
            double longitude = Double.parseDouble(latlng[1]);

            return new LatLng(latitude, longitude);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public boolean isActive() {
        if (this.isActive == 0) {
            return false;
        } else {
            return true;
        }
    }
}