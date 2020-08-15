<?php
abstract class Constants {
    const DB_HOST = 'localhost';
    const DB_USER = 'root';
    const DB_PASS = '';
    const DB_NAME = 'rishtey';
    const SUCCESS = 1;
    const FAILURE = 0;
	const UPLOAD_LIMIT = 5; // 5 in 1 hour
	const DOWNLOAD_LIMIT = 10; // 10 in a day
	const ACCEPTABLE_BIODATA_EXTENSION = array('jpeg', 'jpg', 'png', 'pdf');
	const ACCEPTABLE_BIODATA_TYPE = array('image/jpeg', 'image/jpg', 'image/pjpeg', 'image/x-png', 'image/png', 'application/pdf');
	const ACCEPTABLE_BIODATA_SIZE = 5; // in MB
	const ACCEPTABLE_IMAGE_EXTENSION = array('jpeg', 'jpg', 'png');
	const ACCEPTABLE_IMAGE_TYPE = array('image/jpeg', 'image/jpg', 'image/pjpeg', 'image/x-png', 'image/png');
	const ACCEPTABLE_IMAGE_SIZE = 3; // in MB
	const UPLOAD_DIR_NAME = 'upload';
}
