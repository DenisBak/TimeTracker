package com.denis.domain;

import com.denis.domain.configs.ConfigFactory;
import com.denis.domain.configs.ConfigNames;
import com.denis.domain.dao.track.TrackDto;
import com.denis.domain.dao.user.UserDao;
import com.denis.domain.dao.user.UserDto;
import com.denis.domain.exceptions.DAOException;
import com.denis.domain.exceptions.DomainException;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {
    private List<Track> tracks;

    private int id;
    private String username;
    private String password;
    private String name;

    private static final UserDao dao = UserDao.getInstance();
    private static final Logger logger = LogManager.getLogger();
    private static final Configuration loggerMessages = ConfigFactory.getConfigByName(ConfigNames.LOGGER_MESSAGES);


    protected User(int id, String username, String password, String name) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setName(name);
    }

    private User(UserDto dto) {
        this(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getName());
    }

    public static User createUser(String username, String password, String name) throws DomainException {
        User user;
        try {
            int id = dao.retrieveId(username);
            dao.createUser(username, password, name);
            user = new User(id, username, password, name);
        } catch (DAOException e) {
            throw new DomainException(e);
        }
        return user;
    }

    public static User getUser(String username, String password) throws DomainException {
        UserDto dto;
        User user;
        try {
            dto = dao.retrieveUserDto(username, password);
            user = new User(dto);
            user.setTracks(Track.getTracksByUserId(dto.getId()));
            logger.debug(loggerMessages.getString("userRetrieved") + user);
        } catch (DAOException e) {
            throw new DomainException(e);
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "tracks=" + tracks +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && username.equals(user.username) && password.equals(user.password);
    }

    public void addTrack(Track t) {
        assert t != null;
        tracks.add(t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Track> getTracks() {
        return new ArrayList<>(tracks);
    }
}
