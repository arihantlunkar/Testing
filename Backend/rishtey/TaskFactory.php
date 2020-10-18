<?php
require_once ROOT_PATH . 'LoginTask.php';
require_once ROOT_PATH . 'UploadTask.php';
require_once ROOT_PATH . 'DeleteTask.php';
require_once ROOT_PATH . 'DownloadTask.php';
require_once ROOT_PATH . 'FilterTask.php';
require_once ROOT_PATH . 'SearchTask.php';
require_once ROOT_PATH . 'DisplayTask.php';
require_once ROOT_PATH . 'AdminSaveTask.php';
require_once ROOT_PATH . 'AdminRetrieveTask.php';

class TaskFactory
{
	final public static function get($jsonDecodedPOSTData) : Task
	{
		$task = null;
		if (!isset($jsonDecodedPOSTData->task)) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Missing tag in JSON object',
				1003,
				'Missing \'task\' tag in input JSON object or Task is empty'
			);
		} 
		switch (strtolower($jsonDecodedPOSTData->task)) {
			case 'login':
				$task = new LoginTask($jsonDecodedPOSTData);
				break;

			case 'upload':
				$task = new UploadTask($jsonDecodedPOSTData);
				break;

			case 'delete':
				$task = new DeleteTask($jsonDecodedPOSTData);
				break;

			case 'download':
				$task = new DownloadTask($jsonDecodedPOSTData);
				break;

			case 'filter':
				$task = new FilterTask($jsonDecodedPOSTData);
				break;

			case 'search':
				$task = new SearchTask($jsonDecodedPOSTData);
				break;

			case 'display':
				$task = new DisplayTask($jsonDecodedPOSTData);
				break;
			
			case 'admin_save':
				$task = new AdminSaveTask($jsonDecodedPOSTData);
				break;
			
			case 'admin_retrieve':
				$task = new AdminRetrieveTask($jsonDecodedPOSTData);
				break;
				
			default:
				throw new CustomMessage(
					__class__,
					Constants::FAILURE,
					'Unknown task',
					1004,
					'No task named \'' . $jsonDecodedPOSTData->task . '\' found'
				);
		}
		return $task;
	}
}
