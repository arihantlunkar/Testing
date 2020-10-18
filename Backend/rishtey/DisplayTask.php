<?php
require_once ROOT_PATH . 'Task.php';

class DisplayTask extends Task
{
	public function trigger() : void
	{
		$stmt = $this->mConnection->prepare('SELECT * FROM `biodata`, (SELECT `biodataID` from upload ORDER BY `uploadTime` DESC LIMIT ?, 5) as extractedBiodata where biodata.id = extractedBiodata.biodataID');
		if (false === $stmt) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'DB error',
				1005,
				'Syntax error or missing privileges err2or => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$bindResult = $stmt->bind_param('s', $this->mJSONDecodedPOSTData->currentPosition);
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
		throw new CustomMessage(__CLASS__, Constants::SUCCESS, 'Display Results', 3003, '', $rows);
	}
	protected function validateData() : void {}
}
