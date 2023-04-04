import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useParams } from "react-router-dom";
import { Col, Container, Row } from "react-bootstrap";

import products from "../../assets/fake-data/products";
import { cartActions } from "../../redux/store/shopping-cart/cartSlice";
import Helmet from "../../components/helmet/Helmet";
import CommonAd from "../../components/ui/common-ad/CommonAd";
import ProductCard from "../../components/ui/product-card/ProductCard";
import "./FoodDetails.css";
import AlertText from "../../components/alerts/alert-text/AlertText";
import { useFormValidation } from "../../hooks/validationForms";

const FoodDetails = () => {
	const [tab, setTab] = useState("desc");
	const [enteredName, setEnteredName] = useState("");
	const [reviewMsg, setReviewMsg] = useState("");
	const { id } = useParams();
	const dispatch = useDispatch();

	const product = products.find((product) => product.id === id);
	const { title, price, category, desc, image01 } = product;
	const [previewImg, setPreviewImg] = useState(image01);

	const relatedProduct = products.filter((item) => category === item.category);

	const addItem = () => {
		dispatch(
			cartActions.addItem({
				id,
				title,
				price,
				image01,
			})
		);
	};

	useEffect(() => {
		setPreviewImg(product.image01);
	}, [product]);

	useEffect(() => {
		window.scrollTo(0, 0);
	}, [product]);

	const { email, emailDirty, emailError, formValid, emailHandler } =
		useFormValidation();

	const submitHandler = (e) => {
		e.preventDefault();

		console.log(enteredName, emailHandler, reviewMsg);
	};

	return (
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
									<img src={product.image01} alt="" className="img__small" />
								</div>
								<div
									className="img__item mb-3"
									onClick={() => setPreviewImg(product.image02)}
								>
									<img src={product.image02} alt="" className="img__small" />
								</div>

								<div
									className="img__item"
									onClick={() => setPreviewImg(product.image03)}
								>
									<img src={product.image03} alt="" className="img__small" />
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
									<p>{desc}</p>
								</div>
							) : (
								<div className="tab__form mb-3">
									<div className="review pt-5">
										<p className="user__name mb-0">Jhon Doe</p>
										<p className="user__email">jhon1@gmail.com</p>
										<p className="feedback__text">great product</p>
									</div>

									<div className="review">
										<p className="user__name mb-0">Jhon Doe</p>
										<p className="user__email">jhon1@gmail.com</p>
										<p className="feedback__text">great product</p>
									</div>

									<div className="review">
										<p className="user__name mb-0">Jhon Doe</p>
										<p className="user__email">jhon1@gmail.com</p>
										<p className="feedback__text">great product</p>
									</div>

									<form className="form" onSubmit={submitHandler}>
										<div className="form__group">
											<input
												type="text"
												placeholder="Enter your name"
												onChange={(e) => setEnteredName(e.target.value)}
												required
											/>
										</div>

										<div className="form__group">
											<AlertText
												paramDirty={emailDirty}
												paramError={emailError}
												paramSuccess="E-mail is good!"
											/>
											<input
												value={email}
												name="email"
												type="email"
												placeholder="Enter your email"
												onChange={(e) => emailHandler(e)}
												required
											/>
										</div>

										<div className="form__group">
											<textarea
												rows={5}
												type="text"
												placeholder="Write your review"
												onChange={(e) => setReviewMsg(e.target.value)}
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
								</div>
							)}
						</Col>

						<Col lg="12" className="mb-5 mt-4">
							<h2 className="related__Product__title">You might also like</h2>
						</Col>

						{relatedProduct.map((item) => (
							<Col lg="3" md="4" sm="6" xs="6" className="mb-4" key={item.id}>
								<ProductCard item={item} />
							</Col>
						))}
					</Row>
				</Container>
			</section>
		</Helmet>
	);
};

export default FoodDetails;
