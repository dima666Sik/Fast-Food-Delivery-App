import React, { useState } from "react";
import { Container, Row, Col } from "react-bootstrap";

import Helmet from "../../components/helmet/Helmet";
import ProductCard from "../../components/ui/product-card/ProductCard";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import products from "../../assets/fake-data/products.js";
import "./AllFoods.css";
import Pagination from "../../components/pagination/Pagination";

const AllFoods = () => {
	const [searchTerm, setSearchTerm] = useState("");
	const [productData, setProductData] = useState(products);
	const [loading, setLoading] = useState(false);
	const [currentPage, setCurrentPage] = useState(1);
	const [productsPerPage] = useState(8);

	const filteredProducts = productData.filter((item) => {
		return item.title.toLowerCase().includes(searchTerm.toLowerCase().trim());
	});

	const lastProductIndex = currentPage * productsPerPage;
	const firstProductIndex = lastProductIndex - productsPerPage;
	const currentProducts = filteredProducts.slice(
		firstProductIndex,
		lastProductIndex
	);

	const pagination = (paginationNumber) => setCurrentPage(paginationNumber);

	return (
		<Helmet title="All-Foods">
			<CommonAd title="All Foods" />
			<section>
				<Container>
					<Row>
						<Col lg="6" md="6" sm="6" xs="12">
							<div className="search__widget d-flex align-items-center justify-content-between">
								<input
									className="w-100"
									type="text"
									placeholder="I'm looking for...."
									value={searchTerm}
									onChange={(e) => setSearchTerm(e.target.value)}
								/>
								<span>
									<i className="bi bi-search"></i>
								</span>
							</div>
						</Col>
						<Col lg="6" md="6" sm="6" xs="12" className="mb-5">
							<div className="sorting__widget text-end">
								<select className="w-50">
									<option>Default</option>
									<option value="ascending">Alphabetically, A-Z</option>
									<option value="descending">Alphabetically, Z-A</option>
									<option value="high-price">High Price</option>
									<option value="low-price">Low Price</option>
									<option value="high-likes">High Likes</option>
									<option value="low-likes">Low Likes</option>
								</select>
							</div>
						</Col>

						{currentProducts.map((item) => (
							<Col lg="3" md="4" sm="6" xs="6" key={item.id} className="mb-4">
								<ProductCard item={item} key={item.id} />
							</Col>
						))}
						<Pagination
							productPerPage={productsPerPage}
							totalProduct={filteredProducts.length}
							pagination={pagination}
						/>
					</Row>
				</Container>
			</section>
		</Helmet>
	);
};

export default AllFoods;
