<?php
require_once ROOT_PATH . 'Task.php';

class AdminRetrieveTask extends Task
{
	public function trigger() : void
	{
		if($this->mJSONDecodedPOSTData->requestType === 'ids')
		{
			$stmt = $this->mConnection->prepare('SELECT `biodataID` FROM `verification` WHERE `isVerified` = 0');
			if (false === $stmt) {
				throw new CustomMessage(
					__class__,
					Constants::FAILURE,
					'DB error',
					1005,
					'Syntax error or missing privileges err2or => ' . htmlspecialchars($this->mConnection->error)
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
			$rows = array();
			while($r = $result->fetch_assoc()) {
				$rows[] = $r;
			}
			throw new CustomMessage(__CLASS__, Constants::SUCCESS, 'List of Ids', 3004, '', $rows);
		}
		else if($this->mJSONDecodedPOSTData->requestType === 'id')
		{
			$stmt = $this->mConnection->prepare('SELECT `biodataLink`, `image1Link`, `image2Link`, `image3Link`, `image4Link`, `image5Link` FROM `biodata` WHERE `id` =  ?');
			if (false === $stmt) {
				throw new CustomMessage(
					__class__,
					Constants::FAILURE,
					'DB error',
					1005,
					'Syntax error or missing privileges err2or => ' . htmlspecialchars($this->mConnection->error)
				);
			}
			$bindResult = $stmt->bind_param('s', $this->mJSONDecodedPOSTData->id);
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
			$rows = array();
			while($r = $result->fetch_assoc()) {
				$rows[] = $r;
			}
			throw new CustomMessage(__CLASS__, Constants::SUCCESS, 'Link details', 3004, '', $rows);
		}
	}
	protected function validateData() : void {}
}
