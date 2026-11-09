PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS unit (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    code TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS unit_translation (
                                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                unit_id INTEGER NOT NULL,
                                                lang TEXT NOT NULL,
                                                name TEXT NOT NULL,
                                                description TEXT NOT NULL,

                                                UNIQUE(unit_id, lang),

    FOREIGN KEY(unit_id)
    REFERENCES unit(id)
    );

INSERT OR IGNORE INTO unit(code) VALUES
('CELSIUS'),
('FAHRENHEIT'),
('KELVIN'),
('DISTANCE'),
('TIME'),
('SPEED');


-- CELSIUS

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'en', 'Celsius', 'Temperature in degrees Celsius'
FROM unit WHERE code = 'CELSIUS';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'fi', 'Celsius', 'Lämpötila Celsius-asteina'
FROM unit WHERE code = 'CELSIUS';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'sv', 'Celsius', 'Temperatur i grader Celsius'
FROM unit WHERE code = 'CELSIUS';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ja', '摂氏', '摂氏で表した温度'
FROM unit WHERE code = 'CELSIUS';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ar', 'مئوية', 'درجة الحرارة بالمئوية'
FROM unit WHERE code = 'CELSIUS';


-- FAHRENHEIT

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'en', 'Fahrenheit', 'Temperature in degrees Fahrenheit'
FROM unit WHERE code = 'FAHRENHEIT';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'fi', 'Fahrenheit', 'Lämpötila Fahrenheit-asteina'
FROM unit WHERE code = 'FAHRENHEIT';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'sv', 'Fahrenheit', 'Temperatur i grader Fahrenheit'
FROM unit WHERE code = 'FAHRENHEIT';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ja', '華氏', '華氏で表した温度'
FROM unit WHERE code = 'FAHRENHEIT';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ar', 'فهرنهايت', 'درجة الحرارة بالفهرنهايت'
FROM unit WHERE code = 'FAHRENHEIT';


-- KELVIN

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'en', 'Kelvin', 'Temperature in Kelvin'
FROM unit WHERE code = 'KELVIN';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'fi', 'Kelvin', 'Lämpötila kelvineinä'
FROM unit WHERE code = 'KELVIN';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'sv', 'Kelvin', 'Temperatur i Kelvin'
FROM unit WHERE code = 'KELVIN';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ja', 'ケルビン', 'ケルビンで表した温度'
FROM unit WHERE code = 'KELVIN';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ar', 'كلفن', 'درجة الحرارة بالكلفن'
FROM unit WHERE code = 'KELVIN';


-- DISTANCE

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'en', 'Kilometres', 'Distance in kilometres'
FROM unit WHERE code = 'DISTANCE';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'fi', 'Kilometrit', 'Matka kilometreinä'
FROM unit WHERE code = 'DISTANCE';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'sv', 'Kilometer', 'Avstånd i kilometer'
FROM unit WHERE code = 'DISTANCE';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ja', 'キロメートル', 'キロメートル単位の距離'
FROM unit WHERE code = 'DISTANCE';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ar', 'كيلومتر', 'المسافة بالكيلومتر'
FROM unit WHERE code = 'DISTANCE';


-- TIME

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'en', 'Hours', 'Time in hours'
FROM unit WHERE code = 'TIME';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'fi', 'Tunnit', 'Aika tunteina'
FROM unit WHERE code = 'TIME';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'sv', 'Timmar', 'Tid i timmar'
FROM unit WHERE code = 'TIME';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ja', '時間', '時間単位'
FROM unit WHERE code = 'TIME';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ar', 'ساعات', 'الوقت بالساعات'
FROM unit WHERE code = 'TIME';


-- SPEED

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'en', 'km/h', 'Speed in kilometres per hour'
FROM unit WHERE code = 'SPEED';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'fi', 'km/h', 'Nopeus kilometreinä tunnissa'
FROM unit WHERE code = 'SPEED';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'sv', 'km/h', 'Hastighet i kilometer per timme'
FROM unit WHERE code = 'SPEED';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ja', 'km/h', '時速キロメートル'
FROM unit WHERE code = 'SPEED';

INSERT OR IGNORE INTO unit_translation(unit_id, lang, name, description)
SELECT id, 'ar', 'كم/ساعة', 'السرعة بالكيلومتر في الساعة'
FROM unit WHERE code = 'SPEED';