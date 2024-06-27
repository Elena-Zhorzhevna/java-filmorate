package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.slf4j.Logger;

import java.time.LocalDate;

public class Film {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Film.class);
    private int id; //идентификатор
    private String name; //название
    private String description; //описание
    private LocalDate releaseDate; //дата релиза
    private int duration; //продолжительность

    @JsonCreator
    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }


    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Film)) return false;
        final Film other = (Film) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$releaseDate = this.getReleaseDate();
        final Object other$releaseDate = other.getReleaseDate();
        if (this$releaseDate == null ? other$releaseDate != null : !this$releaseDate.equals(other$releaseDate))
            return false;
        final Object this$duration = this.getDuration();
        final Object other$duration = other.getDuration();
        if (this$duration == null ? other$duration != null : !this$duration.equals(other$duration)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Film;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $releaseDate = this.getReleaseDate();
        result = result * PRIME + ($releaseDate == null ? 43 : $releaseDate.hashCode());
        final Object $duration = this.getDuration();
        result = result * PRIME + ($duration == null ? 43 : $duration.hashCode());
        return result;
    }

    public String toString() {
        return "Film(id=" + this.getId()
                + ", name=" + this.getName()
                + ", description=" + this.getDescription()
                + ", releaseDate=" + this.getReleaseDate()
                + ", duration=" + this.getDuration()
                + ")";
    }
}
