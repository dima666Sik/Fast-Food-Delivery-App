import React, { useState } from "react";
import "./Pagination.css";

const Pagination = ({ productPerPage, totalProduct, pagination }) => {
	const [activePage, setActivePage] = useState(1);

	const pageNumbers = [];
	for (let i = 1; i <= Math.ceil(totalProduct / productPerPage); i++) {
		pageNumbers.push(i);
	}

	return (
		<div>
			<ul className="pagination d-flex justify-content-center">
				{pageNumbers.map((number) => (
					<li
						className={`page-item ${number === activePage ? "active" : ""}`}
						key={number}
					>
						<button
							href="#"
							className="page-link"
							onClick={() => {
								setActivePage(number);
								pagination(number);
							}}
						>
							{number}
						</button>
					</li>
				))}
			</ul>
		</div>
	);
};

export default Pagination;
