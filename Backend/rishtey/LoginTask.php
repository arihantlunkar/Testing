<?php
require_once ROOT_PATH . 'Task.php';

class LoginTask extends Task
{
	public function trigger() : void
	{
		if ($this->userExists()) {
			$this->update();
		} else {
			$this->insert();
		}
	}
	protected function validateData() : void
	{
		if (empty($this->mJSONDecodedInputData->socialMedia)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'socialMedia\' value is empty');
		} else if (empty($this->mJSONDecodedInputData->socialMediaID)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'socialMediaID\' value is empty');
		} else if (empty($this->mJSONDecodedInputData->name)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'name\' value is empty');
		} else if (empty($this->mJSONDecodedInputData->email)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'email\' value is empty');
		}
		$this->mJSONDecodedInputData->pictureURL = !empty($this->mJSONDecodedInputData->pictureURL) ? $this->mJSONDecodedInputData->pictureURL : null;
		$this->mJSONDecodedInputData->birthday = !empty($this->mJSONDecodedInputData->birthday) ? $this->mJSONDecodedInputData->birthday : null;
		$this->mJSONDecodedInputData->gender = !empty($this->mJSONDecodedInputData->gender) ? $this->mJSONDecodedInputData->gender : null; 
		
		// sanitize
		$this->mJSONDecodedInputData->socialMedia = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->socialMedia));
		$this->mJSONDecodedInputData->socialMediaID = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->socialMediaID));
		$this->mJSONDecodedInputData->name = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->name));
		$this->mJSONDecodedInputData->email = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->email));
		$this->mJSONDecodedInputData->pictureURL = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->pictureURL));
		$this->mJSONDecodedInputData->birthday = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->birthday));
		$this->mJSONDecodedInputData->gender = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->gender));
	}
	private function userExists() : bool
	{
		$stmt = $this->mConnection->prepare('SELECT * FROM `users` where socialMedia=? and socialMediaID=?');
		if (false === $stmt) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'DB error',
				1005,
				'Syntax error or missing privileges error => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$bindResult = $stmt->bind_param('ss', $this->mJSONDecodedInputData->socialMedia, $this->mJSONDecodedInputData->socialMediaID);
		if (false === $bindResult) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Corrupt data',
				2001,
				'Bind failed. Possible reason could be inserted data is corrupted => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$executeResult = $stmt->execute();
		if (false === $executeResult) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Internet connection error',
				2002,
				'Tripping over the network cable => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$result = $stmt->get_result();
		$stmt->close();
		if ($result->num_rows > 0) {
			$row = $result->fetch_assoc();
			$this->mJSONDecodedInputData->id = $row['id'];
			return true;
		}
		return false;
	}
	private function update() : void
	{
		$stmt = $this->mConnection->prepare(
			'Update `users` set `socialMedia`=?, `socialMediaID`=?, `name`=?, `email`=?, `birthday`=?, `gender`=?, `pictureURL`=? where `id`=?'
		);
		if (false === $stmt) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'DB error',
				1005,
				'Syntax error or missing privileges error => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$bindResult = $stmt->bind_param(
			'sssssssi',
			$this->mJSONDecodedInputData->socialMedia,
			$this->mJSONDecodedInputData->socialMediaID,
			$this->mJSONDecodedInputData->name,
			$this->mJSONDecodedInputData->email,
			$this->mJSONDecodedInputData->birthday,
			$this->mJSONDecodedInputData->gender,
			$this->mJSONDecodedInputData->pictureURL,
			$this->mJSONDecodedInputData->id
		);
		if (false === $bindResult) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Corrupt data',
				2001,
				'Bind failed. Possible reason could be inserted data is corrupted => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$executeResult = $stmt->execute();
		if (false === $executeResult) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Internet connection error',
				2002,
				'Tripping over the network cable => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$stmt->close();
		throw new CustomMessage(__class__, Constants::SUCCESS, 'User record updated successfully', 3001);
	}
	private function insert() : void
	{
		$this->mJSONDecodedInputData->id = date('dmYHisu');
		$stmt = $this->mConnection->prepare(
			'INSERT INTO `users`(`id`, `socialMedia`, `socialMediaID`, `name`, `email`, `birthday`, `gender`, `pictureURL`) values (?, ?, ?, ?, ?, ?, ?, ?)'
		);
		if (false === $stmt) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'DB error',
				1005,
				'Syntax error or missing privileges error => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$bindResult = $stmt->bind_param(
			'isssssss',
			$this->mJSONDecodedInputData->id,
			$this->mJSONDecodedInputData->socialMedia,
			$this->mJSONDecodedInputData->socialMediaID,
			$this->mJSONDecodedInputData->name,
			$this->mJSONDecodedInputData->email,
			$this->mJSONDecodedInputData->birthday,
			$this->mJSONDecodedInputData->gender,
			$this->mJSONDecodedInputData->pictureURL
		);
		if (false === $bindResult) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Corrupt data',
				2001,
				'Bind failed. Possible reason could be inserted data is corrupted => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$executeResult = $stmt->execute();
		if (false === $executeResult) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'Internet connection error',
				2002,
				'Tripping over the network cable => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$stmt->close();
		throw new CustomMessage(__class__, Constants::SUCCESS, 'User record inserted successfully', 3002);
	}
}
