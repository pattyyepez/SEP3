﻿using DTOs.HouseReview;

namespace Services;

public interface IHouseReviewService
{
    Task<HouseReviewDto> AddAsync(CreateHouseReviewDto houseReview);
    // Task<HouseReviewDto> UpdateAsync(HouseReviewDto houseReview);
    Task DeleteAsync(int id);
    Task<HouseReviewDto> GetSingleAsync(int id);
    IQueryable<HouseReviewDto> GetAll();
}