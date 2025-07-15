SELECT
    id,
    spot_number,
    spot_type,
    status,
    floor_level,
    hourly_rate,
    created_at,
    updated_at
FROM
    parking_spots
WHERE
    id = /* id */0 