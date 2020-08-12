package com.algo.trading.autobot.carpe.diem.config;

public enum RewardProfile
{
    AGGRESSIVE(3),
    MODERATE(5),
    CONSERVATIVE(8);

    RewardProfile(final int rewardPoint)
    {
        this.rewardPoint = rewardPoint;
    }

    private int rewardPoint;

    public int getPoint()
    {
        return rewardPoint;
    }
}
