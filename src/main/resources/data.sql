-- Insert episodes
INSERT INTO episode (id, season, episode_number, description) VALUES (1, 1, 1, 'Episode 1 of Season 1');
INSERT INTO episode (id, season, episode_number, description) VALUES (2, 1, 2, 'Episode 2 of Season 1');
INSERT INTO episode (id, season, episode_number, description) VALUES (3, 2, 1, 'Episode 1 of Season 2');

-- Insert questions for Episode 1 of Season 1
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (1, 1, 3, 1, 60000, 30, 1);
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (2, 2, 3, 2, 120000, 30, 1);
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (3, 3, 3, 3, 180000, 30, 1);

-- Insert questions for Episode 2 of Season 1
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (4, 1, 3, 1, 60000, 30, 2);
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (5, 2, 3, 2, 120000, 30, 2);
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (6, 3, 3, 3, 180000, 30, 2);

-- Insert questions for Episode 1 of Season 2
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (7, 1, 3, 1, 60000, 30, 3);
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (8, 2, 3, 2, 120000, 30, 3);
INSERT INTO question (id, question_number, number_choices, correct_choice, episode_timestamp, duration_seconds, episode_id) VALUES (9, 3, 3, 3, 180000, 30, 3);
