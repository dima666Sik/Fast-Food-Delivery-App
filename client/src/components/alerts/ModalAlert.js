import React from "react";
import { Modal } from "react-bootstrap";

const ModalAlert = ({ paramTitle, paramBody, onShow, onHide }) => {
	return (
		<div
			className="modal show"
			style={{ display: "block", position: "initial" }}
		>
			<Modal
				aria-labelledby="contained-modal-title-vcenter"
				centered
				show={onShow}
				onHide={onHide}
			>
				<Modal.Header closeButton>
					<Modal.Title>{paramTitle}</Modal.Title>
				</Modal.Header>

				<Modal.Body>
					<p>{paramBody}</p>
				</Modal.Body>
			</Modal>
		</div>
	);
};

export default ModalAlert;
