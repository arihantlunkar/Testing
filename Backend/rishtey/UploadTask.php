<?php
require_once ROOT_PATH . 'Task.php';

class UploadTask extends Task
{
	private $mUploadTime;
	private $mBiodataTargetFile;
	private $mPicture1TargetFile;
	private $mPicture2TargetFile;
	private $mPicture3TargetFile;
	private $mPicture4TargetFile;
	private $mPicture5TargetFile;
	public function trigger() : void
	{
		if ($this->isFakeUser()) {
			throw new CustomMessage(__CLASS__, Constants::FAILURE, 'Fake user upload', 1007);
		}
		if ($this->isUploadLimitReached() && false) {
			throw new CustomMessage(__CLASS__, Constants::FAILURE, 'Upload limit reached', 2005);
		}
		$this->storeFiles();
		$this->updateDB();
		throw new CustomMessage(__CLASS__, Constants::SUCCESS, 'Files uploaded successfully', 3003);
	}
	protected function validateData() : void
	{
		if (empty($this->mJSONDecodedInputData->fromID)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'fromID\' value is empty');
		} else if (empty($this->mJSONDecodedInputData->stringImageBioData)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'stringImageBioData\' value is empty');
		} else if (empty($this->mJSONDecodedInputData->stringImagePicture1)) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'Empty data', 1006, '\'stringImagePicture1\' value is empty');
		} 
		
		// sanitize
		$this->mJSONDecodedInputData->fromID = htmlspecialchars(strip_tags($this->mJSONDecodedInputData->fromID));
	}
	
	private function isFakeUser() : bool
	{
		$stmt = $this->mConnection->prepare('SELECT * FROM `users` where id=?');
		if (false === $stmt) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'DB error',
				1005,
				'Syntax error or missing privileges error => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$bindResult = $stmt->bind_param('s', $this->mJSONDecodedInputData->fromID);
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
		return $result->num_rows <= 0;
	}
	private function isUploadLimitReached() : bool
	{
		$stmt = $this->mConnection->prepare('SELECT * FROM `upload` WHERE `fromID` = ? and uploadTime >= Date_sub(now(),interval 1 hour)');
		if (false === $stmt) {
			throw new CustomMessage(
				__class__,
				Constants::FAILURE,
				'DB error',
				1005,
				'Syntax error or missing privileges error => ' . htmlspecialchars($this->mConnection->error)
			);
		}
		$bindResult = $stmt->bind_param('i', $this->mJSONDecodedInputData->fromID);
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
		if ($result->num_rows >= Constants::UPLOAD_LIMIT) {
			return true;
		}
		return false;
	}
	private function storeFiles() : void
	{
		$this->mUploadTime = date('dmYHisu');
		$baseDir = Constants::UPLOAD_DIR_NAME . "/" . $this->mJSONDecodedInputData->fromID . "/" . $this->mUploadTime;
		$this->mBiodataTargetFile =  $baseDir . "/Biodata." . $this->mJSONDecodedInputData->extensionBioData;
		$this->mPicture1TargetFile = $baseDir . "/Picture1." . $this->mJSONDecodedInputData->extensionPicture1;
		$this->mPicture2TargetFile = !empty($this->mJSONDecodedInputData->stringImagePicture2) ? $baseDir . "/Picture2." . $this->mJSONDecodedInputData->extensionPicture2 : null;
		$this->mPicture3TargetFile = !empty($this->mJSONDecodedInputData->stringImagePicture3) ? $baseDir . "/Picture3." . $this->mJSONDecodedInputData->extensionPicture3 : null;
		$this->mPicture4TargetFile = !empty($this->mJSONDecodedInputData->stringImagePicture4) ? $baseDir . "/Picture4." . $this->mJSONDecodedInputData->extensionPicture4 : null;
		$this->mPicture5TargetFile = !empty($this->mJSONDecodedInputData->stringImagePicture5) ? $baseDir . "/Picture5." . $this->mJSONDecodedInputData->extensionPicture5 : null;
		if (false === is_dir($baseDir)) {
			mkdir($baseDir, 0777, true);
		}
		if (false === file_put_contents($this->mBiodataTargetFile, base64_decode($this->mJSONDecodedInputData->stringImageBioData))) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'File not uploaded successfully', 2007, 'Biodata not uploaded');
		}
		if (false === file_put_contents($this->mPicture1TargetFile, base64_decode($this->mJSONDecodedInputData->stringImagePicture1))) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'File not uploaded successfully', 2007, 'Picture1 not uploaded');
		}
		if (null != $this->mPicture2TargetFile && false === file_put_contents($this->mPicture2TargetFile, base64_decode($this->mJSONDecodedInputData->stringImagePicture2))) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'File not uploaded successfully', 2007, 'Picture2 not uploaded');
		}
		if (null != $this->mPicture3TargetFile && false === file_put_contents($this->mPicture3TargetFile, base64_decode($this->mJSONDecodedInputData->stringImagePicture3))) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'File not uploaded successfully', 2007, 'Picture3 not uploaded');
		}
		if (null != $this->mPicture3TargetFile && false === file_put_contents($this->mPicture4TargetFile, base64_decode($this->mJSONDecodedInputData->stringImagePicture4))) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'File not uploaded successfully', 2007, 'Picture4 not uploaded');
		}
		if (null != $this->mPicture5TargetFile && false === file_put_contents($this->mPicture5TargetFile, base64_decode($this->mJSONDecodedInputData->stringImagePicture5))) {
			throw new CustomMessage(__class__, Constants::FAILURE, 'File not uploaded successfully', 2007, 'Picture5 not uploaded');
		}
	}
	private function updateDB() : void
	{
		$this->updateIntoBiodataTable();
		$this->updateIntoUploadTable();
		$this->updateIntoVerificationTable();
	}
	private function updateIntoBiodataTable() : void
	{
		$stmt = $this->mConnection->prepare(
			'INSERT INTO `biodata`(`id`, `biodataLink`, `image1Link`, `image2Link`, `image3Link`, `image4Link`, `image5Link`) values (?, ?, ?, ?, ?, ?, ?)'
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
			'issssss',
			$this->mUploadTime,
			$this->mBiodataTargetFile,
			$this->mPicture1TargetFile,
			$this->mPicture2TargetFile,
			$this->mPicture3TargetFile,
			$this->mPicture4TargetFile,
			$this->mPicture5TargetFile
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
	}
	private function updateIntoUploadTable() : void
	{
		$stmt = $this->mConnection->prepare(
			'INSERT INTO `upload`(`fromID`, `biodataID`) values (?, ?)'
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
			'ii',
			$this->mJSONDecodedInputData->fromID,
			$this->mUploadTime
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
	}
	private function updateIntoVerificationTable() : void
	{
		$isVerified = 0;
		$stmt = $this->mConnection->prepare(
			'INSERT INTO `verification`(`biodataID`, `isVerified`) values (?, ?)'
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
			'ii',
			$this->mUploadTime,
			$isVerified
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
	}
}
