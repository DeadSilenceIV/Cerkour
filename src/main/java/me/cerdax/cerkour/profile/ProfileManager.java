package me.cerdax.cerkour.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    List<Profile> profiles;

    public ProfileManager() {
        this.profiles = new ArrayList<>();
    }

    public Profile getProfile(UUID uuid) {
        Profile profile = this.profiles.stream().filter(profile1 -> profile1.getUuid().toString().equals(uuid.toString())).findAny().orElse(null);
        if (profile == null) {
            profile = new Profile(uuid);
            this.profiles.add(profile);
        }
        return profile;
    }
}
