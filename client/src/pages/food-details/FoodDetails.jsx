import React, { useEffect, useRef, useState } from "react";
import { Col, Container, Row, Spinner } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";

import { deleteReview } from "../../actions/delete/deleteReview";
import { getAllReviewsToProduct } from "../../actions/get/getAllReviewsToProduct";
import { addReview } from "../../actions/post/addReview";
import { refresh } from "../../actions/post/refresh";
import ModalAlert from "../../components/alerts/ModalAlert";
import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import ProductCard from "../../components/ui/product-card/ProductCard";
import { useGetAllProducts } from "../../hooks/useGetAllProducts";
import { useValidFormsBtn } from "../../hooks/useValidFormsBtn";
import { cartActions } from "../../redux/store/shopping-cart/cartSlice";
import { axiosGetStatusLikes } from "../../redux/store/shopping-cart/cartsLikedSlice";
import { axiosLogout, setUser } from "../../redux/store/user/userSlice";
import "./FoodDetails.css";

const FoodDetails = () => {
	const [tab, setTab] = useState("desc");
	const [reviewMsg, setReviewMsg] = useState("");
	const { id } = useParams();
	const { products, isLoading } = useGetAllProducts();
	const [product, setProduct] = useState({});
	const [relatedProduct, setRelatedProduct] = useState([]);
	const isAuthenticated = useSelector((state) => state.user.isAuthenticated);
	const [reviews, setReviews] = useState([]);
	const [isFetchingComments, setIsFetchingComments] = useState(false);
	const accessToken = useSelector((state) => state.user.accessToken);
	const userEmail = useSelector((state) => state.user.email);
	const userRole = useSelector((state) => state.user.role);
	const dispatch = useDispatch();
	const [showModal, setShowModal] = useState(false);
	const reviewContainerRef = useRef(null);
	const [showTextModal, setShowTextModal] = useState("");
	const [deletedReview, setDeletedReview] = useState(false);
	const navigate = useNavigate();

	useEffect(() => {
		if (!isLoading && products.length > 0) {
			const selectedProduct = products.find(
				(product) => product.id.toString() === id
			);
			console.log(selectedProduct);
			if (selectedProduct) {
				setProduct(selectedProduct);
				const selectedRelPr = products.filter(
					(item) => product.category === item.category
				);
				if (selectedRelPr) {
					setRelatedProduct(selectedRelPr);
				}
			} else {
				navigate("/");
			}
		}
	}, [id, product, isLoading]);

	const { title, price, category, description, image01 } = product;

	const [previewImg, setPreviewImg] = useState(image01);

	const addItem = () => {
		if (product) {
			dispatch(
				cartActions.addItem({
					id,
					title,
					price,
					image01,
				})
			);
		}
	};

	useEffect(() => {
		setPreviewImg(product.image01);
		window.scrollTo(0, 0);
	}, [product]);

	const { formValid, setFormValid } = useValidFormsBtn();

	useEffect(() => {
		if (!reviewMsg) setFormValid(false);
		else setFormValid(true);
	}, [reviewMsg]);

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

	const submitHandler = async (e) => {
		e.preventDefault();

		console.log(accessToken, reviewMsg, product.id);
		if (product?.id) {
			console.log("+");
			const dataProductReview = {
				product_id: product.id,
				review: reviewMsg,
			};

			try {
				setIsFetchingComments(true); // Установка состояния перед началом загрузки комментариев
				const response = await addReview(accessToken, dataProductReview);
				setReviewMsg("");
				console.log(response.data);
			} catch (error) {
				console.log(error, accessToken);
				if (
					error.response.data.message === "Access token has expired" ||
					error.response.data.message === "Access token has revoked"
				) {
					try {
						const response = await refresh(accessToken);

						if (response.status === 200) {
							if (response.data.access_token) {
								// Dispatch the setUser action
								dispatch(
									setUser({
										accessToken: response.data.access_token,
									})
								);
								setShowTextModal(
									"Tokens was Updated, please continue use site."
								);
								setShowModal(true);
							}
						}
					} catch (error) {
						console.log(error);
						throw error; // пробрасываем ошибку выше для обработки в setLikes
					}
				}

				if (
					error.response.data.message ===
						"Access & Refresh tokens have expired" ||
					error.response.data.message === "Access token not found" ||
					error.response.data.message === "Refresh token not found" ||
					error.response.data.message === "Invalid token"
				) {
					setShowTextModal(
						"You don't have rights to review. Please Authorization in this Application..."
					);
					setShowModal(true);
					dispatch(
						axiosLogout({
							accessToken,
						})
					);
					setReviewMsg("");
				}
			} finally {
				setIsFetchingComments(false);
			}
		} else {
			console.error("This product is undefined!");
		}
	};

	useEffect(() => {
		const fetchData = async () => {
			if (product?.id) {
				try {
					const response = await getAllReviewsToProduct(product.id);
					const getReviews = response.data;
					setReviews(getReviews);
					console.log(getReviews);
				} catch (error) {
					if (error.response.status === 404 && reviews.length !== 0) {
						const getReviews = [];
						setReviews(getReviews);
						console.log(getReviews);
					}

					if (
						error.response.data.message === "Access token has expired" ||
						error.response.data.message === "Access token has revoked"
					) {
						try {
							const response = await refresh(accessToken);

							if (response.status === 200) {
								if (response.data.access_token) {
									// Dispatch the setUser action
									dispatch(
										setUser({
											accessToken: response.data.access_token,
										})
									);
									setShowTextModal(
										"Tokens was Updated, please continue use site."
									);
									setShowModal(true);
								}
							}
						} catch (error) {
							console.log(error);
							throw error; // пробрасываем ошибку выше для обработки в setLikes
						}
					}

					if (
						error.response.data.message ===
							"Access & Refresh tokens have expired" ||
						error.response.data.message === "Access token not found" ||
						error.response.data.message === "Refresh token not found" ||
						error.response.data.message === "Invalid token"
					) {
						setShowTextModal(
							"You don't have rights to review. Please Authorization in this Application..."
						);
						setShowModal(true);
						dispatch(
							axiosLogout({
								accessToken,
							})
						);
						setReviewMsg("");
					}
				}
			}
		};
		fetchData();
	}, [product, isFetchingComments, deletedReview]);

	async function handleDeleteClick(review_id) {
		console.log(review_id);
		try {
			await deleteReview(accessToken, product.id, review_id);
			setDeletedReview(!deletedReview);
		} catch (error) {
			if (
				error.response.data.message === "Access token has expired" ||
				error.response.data.message === "Access token has revoked"
			) {
				try {
					const response = await refresh(accessToken);

					if (response.status === 200) {
						if (response.data.access_token) {
							// Dispatch the setUser action
							dispatch(
								setUser({
									accessToken: response.data.access_token,
								})
							);
							setShowTextModal("Tokens was Updated, please continue use site.");
							setShowModal(true);
						}
					}
				} catch (error) {
					console.log(error);
					throw error; // пробрасываем ошибку выше для обработки в setLikes
				}
			}

			if (
				error.response.data.message ===
					"Access & Refresh tokens have expired" ||
				error.response.data.message === "Access token not found" ||
				error.response.data.message === "Refresh token not found" ||
				error.response.data.message === "Invalid token"
			) {
				setShowTextModal(
					"You don't have rights to review. Please Authorization in this Application..."
				);
				setShowModal(true);
				dispatch(
					axiosLogout({
						accessToken,
					})
				);
				setReviewMsg("");
			}
		}
	}

	useEffect(() => {
		if (reviewContainerRef.current) {
			reviewContainerRef.current.scrollTop =
				reviewContainerRef.current.scrollHeight;
		}
	}, [reviews, isFetchingComments, tab]);

	return (
		<>
			{isLoading && !product ? (
				<div className="text-center">
					<Spinner />
				</div>
			) : (
				<>
					<Helmet title="Product-details">
						<CommonAd title={title} />

						<section>
							<Container>
								<Row>
									<Col lg="2" md="2">
										<div className="product__images ">
											<div
												className="img__item mb-3"
												onClick={() => setPreviewImg(product.image01)}
											>
												<img
													src={product.image01}
													alt=""
													className="img__small"
												/>
											</div>
											<div
												className="img__item mb-3"
												onClick={() => setPreviewImg(product.image02)}
											>
												<img
													src={product.image02}
													alt=""
													className="img__small"
												/>
											</div>

											<div
												className="img__item"
												onClick={() => setPreviewImg(product.image03)}
											>
												<img
													src={product.image03}
													alt=""
													className="img__small"
												/>
											</div>
										</div>
									</Col>
									<Col lg="4" md="4">
										<div className="product__main__img">
											<img src={previewImg} alt="" className="img__big" />
										</div>
									</Col>
									<Col lg="6" md="6">
										<div className="single__product__content">
											<h2 className="product__title mb-3">{title}</h2>
											<p className="product__price">
												Price: <span>${price}</span>
											</p>
											<p className="category mb-5">
												Category: <span>{category}</span>
											</p>

											<button onClick={addItem} className="addToCart__btn">
												Add to Cart
											</button>
										</div>
									</Col>
									<Col lg="12">
										<div className="tabs d-flex align-items-center gap-5 py-3">
											<h6
												className={` ${tab === "desc" ? "tab__active" : ""}`}
												onClick={() => setTab("desc")}
											>
												Description
											</h6>
											<h6
												className={` ${tab === "rev" ? "tab__active" : ""}`}
												onClick={() => setTab("rev")}
											>
												Review
											</h6>
										</div>

										{tab === "desc" ? (
											<div className="tab__content">
												<p>{description}</p>
											</div>
										) : isFetchingComments ? (
											<div className="text-center">
												<Spinner />
											</div>
										) : (
											<div className="tab__form mb-3">
												{console.log(reviews)}
												{reviews.length === 0 ? (
													<div className="review pt-5 mb-5 pb-5">
														<p className="user__name text-center my-0">
															Review is absent
														</p>
													</div>
												) : (
													<div
														className="review-container pb-5"
														ref={reviewContainerRef}
													>
														{reviews.map((review) => (
															<div
																className="review-wrap pt-5 px-5"
																key={review.id}
															>
																<div className="review">
																	<div className="review-container-dots-list">
																		<p className="user__name mb-0">
																			{review.first_name} {review.last_name}
																		</p>
																		{review.email === userEmail && (
																			<div className="dropdown">
																				<i
																					className="bi bi-three-dots-vertical dropdown-toggle"
																					data-toggle="dropdown"
																				></i>
																				<ul className="dropdown-menu">
																					<li>
																						<span
																							className="delete-selection"
																							onClick={() =>
																								handleDeleteClick(review.id)
																							}
																						>
																							Delete
																						</span>
																					</li>
																				</ul>
																			</div>
																		)}
																	</div>

																	<p className="user__email">{review.email}</p>
																	<p className="feedback__text">
																		{review.review}
																	</p>
																</div>
															</div>
														))}
													</div>
												)}
												{isAuthenticated && userRole !== "ADMIN" && (
													<form className="form mt-5" onSubmit={submitHandler}>
														<div className="form__group">
															<textarea
																rows={5}
																type="text"
																placeholder="Write your review"
																onChange={(e) => setReviewMsg(e.target.value)}
																value={reviewMsg}
																required
															/>
														</div>

														<button
															type="submit"
															className="addToCart__btn"
															disabled={!formValid}
														>
															Submit
														</button>
													</form>
												)}
											</div>
										)}
									</Col>
									<Col lg="12" className="mb-5 mt-4">
										<h2 className="related__Product__title">
											You might also like
										</h2>
									</Col>

									{relatedProduct.map((item) => (
										<Col
											lg="3"
											md="4"
											sm="6"
											xs="6"
											className="mb-4"
											key={item.id}
										>
											<ProductCard item={item} />
										</Col>
									))}
								</Row>
							</Container>
						</section>
					</Helmet>
				</>
			)}

			{showModal && (
				<ModalAlert
					paramTitle={"Error Authenticated"}
					paramBody={showTextModal}
					onShow={showModal}
					onHide={() => setShowModal(false)}
				/>
			)}
		</>
	);
};

export default FoodDetails;
