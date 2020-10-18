<?php
require_once ROOT_PATH . 'Task.php';

class AdminSaveTask extends Task
{
	public function trigger() : void
	{
		if($this->mJSONDecodedPOSTData->status === "Accept")
		{
			$education = implode(', ', $this->mJSONDecodedPOSTData->educationValue);
			
			$stmt = $this->mConnection->prepare(
				'UPDATE `biodata` SET `name`=?, `place`=?,`age`=?,`height`=?,`occupation`=?,`sex`=?,`dosha`=?,`maritialStatus`=?,`sampradaya`=?,`education`=? WHERE `id` = ?'
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
				'ssisssssssi',
				$this->mJSONDecodedPOSTData->personName,
				$this->mJSONDecodedPOSTData->placeValue,
				$this->mJSONDecodedPOSTData->ageValue,
				$this->mJSONDecodedPOSTData->heightValue,
				$this->mJSONDecodedPOSTData->occupationValue,
				$this->mJSONDecodedPOSTData->sex,
				$this->mJSONDecodedPOSTData->doshaValue,
				$this->mJSONDecodedPOSTData->maritialStatusValue,
				$this->mJSONDecodedPOSTData->sampradayaValue,
				$education,
				$this->mJSONDecodedPOSTData->id
				
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
		
		$stmt = $this->mConnection->prepare(
			'UPDATE `verification` SET `isVerified`=1,`isValid`=?,`verifiedTime`=NOW(),`rejectionReason`=?,`markDeleted`=? WHERE `biodataID`=?'
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
		
		$isValid = ($this->mJSONDecodedPOSTData->status === "Accept") ? 1 : 0;
		$markDeleted = ($this->mJSONDecodedPOSTData->status === "Accept") ? 0 : 1;
		
		$bindResult = $stmt->bind_param(
			'isis',
			$isValid,
			$this->mJSONDecodedPOSTData->rejectReasonValue,
			$markDeleted,
			$this->mJSONDecodedPOSTData->id
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
		throw new CustomMessage(__CLASS__, Constants::SUCCESS, 'User record inserted successfully', 3002);		
	}
	protected function validateData() : void {}
}
