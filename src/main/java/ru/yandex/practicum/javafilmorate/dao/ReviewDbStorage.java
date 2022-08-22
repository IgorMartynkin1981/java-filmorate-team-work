package ru.yandex.practicum.javafilmorate.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.javafilmorate.exeption.NotFoundException;
import ru.yandex.practicum.javafilmorate.exeption.ValidationException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.review.ReviewStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final static int NEW_USEFUL = 0;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public List<Review> getAllReview() {
        String sql = "SELECT * FROM reviews";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> getReview(rs, rowNum))).stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getUseful()).compareTo(Integer.valueOf(o2.getUseful()));
                    return result * -1;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> getAllReviewByIdFilm(Integer filmId, Integer count) {
        String sql = "SELECT * FROM reviews WHERE film_id = ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> getReview(rs, rowNum)), filmId).stream()
                .sorted((o1, o2) -> {
                    int result = Integer.valueOf(o1.getUseful()).compareTo(Integer.valueOf(o2.getUseful()));
                    return result * -1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private static Review getReview(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review(rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getInt("user_id"),
                rs.getInt("film_id"));
        review.setIdReview(rs.getInt("id_review"));
        review.setUseful(rs.getInt("useful"));
        return review;
    }

    @Override
    public Review addReview(Review review) {
        String sqlQuery = "INSERT INTO reviews(CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID_REVIEW"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            stmt.setInt(5, 0);
            return stmt;
        }, keyHolder);
        review.setIdReview(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }


    @Override
    public Review changeReview(Review review) {
            String sql = "UPDATE reviews SET content = ?, is_positive = ? WHERE id_review = ?";
            jdbcTemplate.update(sql,
                    review.getContent(),
                    review.getIsPositive(),
                    review.getIdReview()
            );
            return review;
    }

    @Override
    public void deleteReview(Integer id) {
        String sql = "DELETE FROM reviews WHERE id_review = ?";
        jdbcTemplate.update(sql, id);
    }


    @Override
    public void changeUseful(Integer id, Integer num) {
        String sql = "UPDATE reviews SET useful = useful + ? WHERE id_review = ?";
        jdbcTemplate.update(sql, num, id);
    }

    @Override
    public Review findReviewById(Integer id) {
        String sql = "SELECT * FROM reviews WHERE id_review = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            Review review = new Review(
                    rowSet.getString("content"),
                    rowSet.getBoolean("is_positive"),
                    rowSet.getInt("user_id"),
                    rowSet.getInt("film_id")
            );
            review.setIdReview(rowSet.getInt("id_review"));
            review.setUseful(rowSet.getInt("useful"));
            return review;
        } else {
            throw new NotFoundException("Review с id " + id + " не найден.");
        }
    }
}
