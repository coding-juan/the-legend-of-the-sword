package com.codingjuan.ai;

public class BehaviourTree {
    public BehaviourState state;

    public final float fromLockedToApproach = 1.3f;
    public final float fromApproachedToAttack = 2.1f;
    public final float fromAttackedToApproach = 1.8f;
    public final float fromHitToApproach = 1.9f;

    public float fromLockedToApproachElapsed, fromApproachedToAttackElapsed, fromAttackedToApproachElapsed, fromHitToApproachElapsed;

    public boolean targetLocked, atRange;

    public BehaviourState getState() {
        return state;
    }

    public BehaviourTree(BehaviourState state) {
        this.state = state;
        targetLocked = false;
        atRange = false;

        fromLockedToApproachElapsed = 0f;
        fromApproachedToAttackElapsed = 0f;
        fromAttackedToApproachElapsed = 0f;
        fromHitToApproachElapsed = 0f;
    }

    public void hit() {
        state = BehaviourState.HIT;
    }

    public void knockBackCompleted() {
        state = BehaviourState.HIT_COMPLETE;
    }

    public void attackCompleted() {
        state = BehaviourState.ATTACK_COMPLETE;
    }

    public void update(float deltaTime) {
        if (!targetLocked && !state.equals(BehaviourState.PATROLING))
            state = BehaviourState.PATROLING;

        switch (state) {
            case PATROLING:
                if (targetLocked)
                    state = BehaviourState.TARGET_LOCKED;
                break;

            case TARGET_LOCKED:
                fromLockedToApproachElapsed += deltaTime;

                if (fromLockedToApproachElapsed >= fromLockedToApproach) {
                    state = BehaviourState.APPROACHING;
                    fromLockedToApproachElapsed = 0f;
                }
                break;

            case APPROACHING:
                if (atRange) {
                    fromApproachedToAttackElapsed += deltaTime;

                    if (fromApproachedToAttackElapsed >= fromApproachedToAttack) {
                        state = BehaviourState.ATTACKING;
                        fromApproachedToAttackElapsed = 0f;
                    }
                } else {
                    fromApproachedToAttackElapsed = 0f;
                }
                break;

            case ATTACKING:
                if (!atRange)
                    state = BehaviourState.TARGET_LOCKED;
                break;

            case ATTACK_COMPLETE:
                fromAttackedToApproachElapsed += deltaTime;

                if (fromAttackedToApproachElapsed >= fromAttackedToApproach) {
                    state = BehaviourState.APPROACHING;
                    fromAttackedToApproachElapsed = 0f;
                }
                break;

            case HIT:
                fromLockedToApproachElapsed = 0f;
                fromApproachedToAttackElapsed = 0f;
                fromAttackedToApproachElapsed = 0f;

                break;

            case HIT_COMPLETE:
                fromHitToApproachElapsed += deltaTime;

                if (fromHitToApproachElapsed >= fromHitToApproach) {
                    state = BehaviourState.APPROACHING;
                    fromHitToApproachElapsed = 0f;
                }
                break;
        }
    }
}
