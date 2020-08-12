package com.algo.trading.autobot.carpe.diem.config;

public enum RiskProfile
{
    AGGRESSIVE(5),
    MODERATE(8),
    CONSERVATIVE(13);

    RiskProfile(final int riskPoint)
    {
        this.riskPoint = riskPoint;
    }

    private int riskPoint;

    public int getPoint()
    {
        return riskPoint;
    }
}
