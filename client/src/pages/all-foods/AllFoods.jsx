import React, { useEffect, useState } from "react";
import { Container, Row, Col, Spinner } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";

import Helmet from "../../components/helmet/Helmet";
import ProductCard from "../../components/ui/product-card/ProductCard";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import "./AllFoods.css";
import Pagination from "../../components/pagination/Pagination";
import { useGetAllProducts } from "../../hooks/useGetAllProducts";
import { axiosGetStatusLikes } from "../../redux/store/shopping-cart/cartsLikedSlice";
import { getSortedChoiceProducts } from "../../actions/get/getSortedChoiceProducts";

const AllFoods = () => {
	const { products, isLoading } = useGetAllProducts();
	const [isLoadingSort, setIsLoadingSort] = useState(false);
	const [searchTerm, setSearchTerm] = useState("");
	const [productData, setProductData] = useState([]);
	const [currentPage, setCurrentPage] = useState(1);
	const [productsPerPage] = useState(8);
	const [sortedChoice, setSortedChoice] = useState("default");
	const dispatch = useDispatch();

	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const accessToken = useSelector((state) => state.user.accessToken);

	useEffect(() => {
		if (!isLoading) {
			if (sortedChoice === "default") {
				setProductData(products);
				return;
			}

			setIsLoadingSort(true);
			const fetchData = async () => {
				try {
					const updatedProducts = await getSortedChoiceProducts(sortedChoice);
					setProductData(updatedProducts);
				} catch (error) {
					console.log(error);
				} finally {
					setIsLoadingSort(false);
				}
			};
			fetchData();
		}
	}, [sortedChoice, isLoading]);

	const filteredProducts = productData.filter((item) => {
		return item.title.toLowerCase().includes(searchTerm.toLowerCase().trim());
	});

	const lastProductIndex = currentPage * productsPerPage;
	const firstProductIndex = lastProductIndex - productsPerPage;
	const currentProducts = filteredProducts.slice(
		firstProductIndex,
		lastProductIndex
	);
	console.log(currentProducts);
	const pagination = (paginationNumber) => setCurrentPage(paginationNumber);

	useEffect(() => {
		window.scrollTo(0, 0);
	}, []);

	useEffect(() => {
		console.log(isAuthenticated);
		if (isAuthenticated) {
			dispatch(
				axiosGetStatusLikes({
					accessToken: accessToken,
				})
			);
		}
	}, [accessToken]);

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
								<select
									className="w-50"
									value={sortedChoice}
									onChange={(e) => setSortedChoice(e.target.value)}
								>
									<option value="default">Default</option>
									<option value="ascending">Alphabetically, A-Z</option>
									<option value="descending">Alphabetically, Z-A</option>
									<option value="high-price">High Price</option>
									<option value="low-price">Low Price</option>
									<option value="high-likes">High Likes</option>
									<option value="low-likes">Low Likes</option>
								</select>
							</div>
						</Col>

						{isLoading || isLoadingSort || !currentProducts ? (
							<div className="text-center">
								<Spinner />
							</div>
						) : currentProducts.length === 0 ? (
							<div className="review pt-5 mb-5 pb-5">
								<p className="user__name text-center my-0">Foods is absent</p>
							</div>
						) : (
							currentProducts.map((item) => (
								<Col lg="3" md="4" sm="6" xs="6" key={item.id} className="mb-4">
									<ProductCard item={item} key={item.id} />
								</Col>
							))
						)}
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
