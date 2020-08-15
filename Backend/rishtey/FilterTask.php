<?php
require_once ROOT_PATH . 'Task.php';

class FilterTask extends Task
{
	public function trigger() : void
	{
		throw new CustomMessage(__CLASS__, Constants::FAILURE, 'Not implemented', 1100);
	}
	protected function validateData() : void {}
}
