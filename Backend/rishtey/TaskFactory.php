<?php
require_once ROOT_PATH . 'LoginTask.php';
require_once ROOT_PATH . 'UploadTask.php';
require_once ROOT_PATH . 'DeleteTask.php';
require_once ROOT_PATH . 'DownloadTask.php';
require_once ROOT_PATH . 'FilterTask.php';
require_once ROOT_PATH . 'SearchTask.php';
require_once ROOT_PATH . 'DisplayTask.php';

class TaskFactory
{
	final public static function get($jsonDecodedPOSTData) : Task
	{
		$task = null;
		if (!isset($jsonDecodedPOSTData->input->task)) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Missing tag in JSON object',
				1003,
				'Missing \'task\' tag in input JSON object or Task is empty'
			);
		} else if (!isset($jsonDecodedPOSTData->input->data)) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Missing tag in JSON object',
				1003,
				'Missing \'data\' tag in input JSON object or Task is empty'
			);
		}
		switch (strtolower($jsonDecodedPOSTData->input->task)) {
			case 'login':
				$task = new LoginTask($jsonDecodedPOSTData->input->data);
				break;

			case 'upload':
				$task = new UploadTask($jsonDecodedPOSTData->input->data);
				break;

			case 'delete':
				$task = new DeleteTask($jsonDecodedPOSTData->input->data);
				break;

			case 'download':
				$task = new DownloadTask($jsonDecodedPOSTData->input->data);
				break;

			case 'filter':
				$task = new FilterTask($jsonDecodedPOSTData->input->data);
				break;

			case 'search':
				$task = new SearchTask($jsonDecodedPOSTData->input->data);
				break;

			case 'display':
				$task = new DisplayTask($jsonDecodedPOSTData->input->data);
				break;

			default:
				throw new CustomMessage(
					__class__,
					Constants::FAILURE,
					'Unknown task',
					1004,
					'No task named \'' . $jsonDecodedPOSTData->input->task . '\' found'
				);
		}
		return $task;
	}
}
