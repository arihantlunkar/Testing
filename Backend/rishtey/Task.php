<?php
require_once ROOT_PATH . 'Constants.php';
require_once ROOT_PATH . 'CustomMessage.php';
abstract class Task
{
	protected $mConnection;
	protected $mJSONDecodedInputData;
	public function __construct($jsonDecodedInputData)
	{
		$this->mConnection = new mysqli(Constants::DB_HOST, Constants::DB_USER, Constants::DB_PASS, Constants::DB_NAME);
		if ($this->mConnection && $this->mConnection->connect_error) {
			throw new CustomMessage(__CLASS__, Constants::FAILURE, 'Database connection failed', 1002, $this->mConnection->connect_error);
		}
		$this->mJSONDecodedInputData = $jsonDecodedInputData;
		$this->validateData();
	}
	public function __destruct()
	{
		if ($this->mConnection) {
			$this->mConnection->close();
		}
	}
	abstract public function trigger() : void;
	abstract protected function validateData() : void;
}
