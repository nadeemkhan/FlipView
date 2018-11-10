package com.denzsbs.flipviewlib


import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.VelocityTrackerCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*


class FlipView : ConstraintLayout {
    internal var downX = 0f
    internal var upX = 0f
    private val animFlipHorizontalOutId = R.animator.animation_horizontal_flip_out
    private val animFlipHorizontalInId = R.animator.animation_horizontal_flip_in
    private val animFlipHorizontalRightOutId = R.animator.animation_horizontal_right_out
    private val animFlipHorizontalRightInId = R.animator.animation_horizontal_right_in
    private val animFlipVerticalOutId = R.animator.animation_vertical_flip_out
    private val animFlipVerticalInId = R.animator.animation_vertical_flip_in
    private val animFlipVerticalFrontOutId = R.animator.animation_vertical_front_out
    private val animFlipVerticalFrontInId = R.animator.animation_vertical_flip_front_in
    private var mSetRightOut: AnimatorSet? = null
    private var mSetLeftIn: AnimatorSet? = null
    private var mSetTopOut: AnimatorSet? = null
    private var mSetBottomIn: AnimatorSet? = null
    private var mIsBackVisible = false
    private var mCardFrontLayout: View? = null
    private var mCardBackLayout: View? = null
    private var flipType: String? = "vertical"
    private var flipTypeFrom: String? = "right"
    /**
     * Whether view is set to flip on touch or not.
     *
     * @return true or false
     */
    /**
     * Set whether view should be flipped on touch or not!
     *
     * @param flipOnTouch value (true or false)
     */
    var isFlipOnTouch: Boolean = false
    private var flipDuration: Int = 0
    /**
     * Returns whether flip is enabled or not!
     *
     * @return true or false
     */
    /**
     * Enable / Disable flip view.
     *
     * @param flipEnabled true or false
     */
    var isFlipEnabled: Boolean = false
    private var mContext: Context? = null
    private val x1: Float = 0.toFloat()
    private val y1: Float = 0.toFloat()
    /**
     * Returns which flip state is currently on of the flip view.
     *
     * @return current state of flip view
     */
    var currentFlipState = FlipState.FRONT_SIDE
        private set
    var onFlipListener: OnFlipAnimationListener? = null
    private val mDetector: GestureDetector? = null
    private val isHorizontalFlip = false
    private var mVelocityTracker: VelocityTracker? = null

    /**
     * Returns true if the front side of flip view is visible.
     *
     * @return true if the front side of flip view is visible.
     */
    val isFrontSide: Boolean
        get() = currentFlipState == FlipState.FRONT_SIDE

    /**
     * Returns true if the back side of flip view is visible.
     *
     * @return true if the back side of flip view is visible.
     */
    val isBackSide: Boolean
        get() = currentFlipState == FlipState.BACK_SIDE

    constructor(context: Context) : super(context) {
        this.mContext = context
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mContext = context
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // Setting Defaul Values
        isFlipOnTouch = true
        flipDuration = DEFAULT_FLIP_DURATION
        isFlipEnabled = true

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.flip_view, 0, 0)
            try {
                isFlipOnTouch = attrArray.getBoolean(R.styleable.flip_view_flipOnTouch, true)
                flipDuration = attrArray.getInt(
                    R.styleable.flip_view_flipDuration,
                    DEFAULT_FLIP_DURATION
                )
                isFlipEnabled = attrArray.getBoolean(R.styleable.flip_view_flipEnabled, true)
                flipType = attrArray.getString(R.styleable.flip_view_flipType)
                flipTypeFrom = attrArray.getString(R.styleable.flip_view_flipFrom)

                if (TextUtils.isEmpty(flipType)) {
                    flipType = "vertical"
                }
                if (TextUtils.isEmpty(flipTypeFrom)) {
                    flipTypeFrom = "left"
                }
            } finally {
                attrArray.recycle()
            }
        }

        loadAnimations()

    }

    protected override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount > 2) {
            throw IllegalStateException("FlipView can host only two direct children!")
        }

        findViews()
        changeCameraDistance()
    }

    override fun addView(v: View, pos: Int, params: ViewGroup.LayoutParams) {
        if (childCount === 2) {
            throw IllegalStateException("FlipView can host only two direct children!")
        }

        super.addView(v, pos, params)

        findViews()
        changeCameraDistance()
    }

    override fun removeView(v: View) {
        super.removeView(v)

        findViews()
    }

    override fun removeAllViewsInLayout() {
        super.removeAllViewsInLayout()

        // Reset the state
        currentFlipState = FlipState.FRONT_SIDE

        findViews()
    }

    private fun findViews() {
        // Invalidation since we use this also on removeView
        mCardBackLayout = null
        mCardFrontLayout = null

        val childs = getChildCount()
        if (childs < 1) {
            return
        }

        if (childs < 2) {
            // Only invalidate flip state if we have a single child
            currentFlipState = FlipState.FRONT_SIDE

            mCardFrontLayout = getChildAt(0)
        } else if (childs == 2) {
            mCardFrontLayout = getChildAt(1)
            mCardBackLayout = getChildAt(0)
        }

        if (!isFlipOnTouch) {
            mCardFrontLayout?.visibility = VISIBLE

            if (mCardBackLayout != null) {
                mCardBackLayout?.visibility = GONE
            }
        }
    }

    private fun loadAnimations() {
        if (flipType?.equals("horizontal", ignoreCase = true) == true) {

            if (flipTypeFrom?.equals("left", ignoreCase = true) == true) {
                mSetRightOut = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipHorizontalOutId
                ) as AnimatorSet
                mSetLeftIn = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipHorizontalInId
                ) as AnimatorSet
            } else {
                mSetRightOut = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipHorizontalRightOutId
                ) as AnimatorSet
                mSetLeftIn = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipHorizontalRightInId
                ) as AnimatorSet
            }


            if (mSetRightOut == null || mSetLeftIn == null) {
                throw RuntimeException(
                    "No Animations Found! Please set Flip in and Flip out animation Ids."
                )
            }

            mSetRightOut?.removeAllListeners()
            mSetRightOut?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {

                    if (currentFlipState == FlipState.FRONT_SIDE) {
                        mCardBackLayout?.visibility = GONE
                        mCardFrontLayout?.visibility = VISIBLE

                        if (onFlipListener != null)
                            onFlipListener?.onViewFlipCompleted(
                                this@FlipView,
                                FlipState.FRONT_SIDE
                            )
                    } else {
                        mCardBackLayout?.visibility = VISIBLE
                        mCardFrontLayout?.visibility = GONE

                        if (onFlipListener != null)
                            onFlipListener?.onViewFlipCompleted(
                                this@FlipView,
                                FlipState.BACK_SIDE
                            )
                    }
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })
            setFlipDuration(flipDuration)
        } else {

            if (!TextUtils.isEmpty(flipTypeFrom) && flipTypeFrom?.equals(
                    "front",
                    ignoreCase = true
                ) == true
            ) {
                mSetTopOut = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipVerticalFrontOutId
                ) as AnimatorSet
                mSetBottomIn = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipVerticalFrontInId
                ) as AnimatorSet
            } else {
                mSetTopOut = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipVerticalOutId
                ) as AnimatorSet
                mSetBottomIn = AnimatorInflater.loadAnimator(
                    this.mContext,
                    animFlipVerticalInId
                ) as AnimatorSet
            }

            if (mSetTopOut == null || mSetBottomIn == null) {
                throw RuntimeException(
                    "No Animations Found! Please set Flip in and Flip out animation Ids."
                )
            }

            mSetTopOut?.removeAllListeners()
            mSetTopOut?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {

                    if (currentFlipState == FlipState.FRONT_SIDE) {
                        mCardBackLayout?.visibility = GONE
                        mCardFrontLayout?.visibility = VISIBLE

                        if (onFlipListener != null)
                            onFlipListener?.onViewFlipCompleted(
                                this@FlipView,
                                FlipState.FRONT_SIDE
                            )
                    } else {
                        mCardBackLayout?.visibility = VISIBLE
                        mCardFrontLayout?.visibility = GONE

                        if (onFlipListener != null)
                            onFlipListener?.onViewFlipCompleted(
                                this@FlipView,
                                FlipState.BACK_SIDE
                            )
                    }
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })
            setFlipDuration(flipDuration)
        }
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = getResources().getDisplayMetrics().density * distance

        if (mCardFrontLayout != null) {
            mCardFrontLayout?.cameraDistance = scale
        }
        if (mCardBackLayout != null) {
            mCardBackLayout?.cameraDistance = scale
        }
    }

    /**
     * Play the animation of flipping and flip the view for one side!
     */
    fun flipTheView() {
        if (!isFlipEnabled || getChildCount() < 2) return

        if (flipType?.equals("horizontal", ignoreCase = true) == true) {
            if (mSetRightOut?.isRunning == true || mSetLeftIn?.isRunning == true) return

            mCardBackLayout?.visibility = VISIBLE
            mCardFrontLayout?.visibility = VISIBLE

            if (currentFlipState == FlipState.FRONT_SIDE) {
                // From front to back
                mSetRightOut?.setTarget(mCardFrontLayout)
                mSetLeftIn?.setTarget(mCardBackLayout)
                mSetRightOut?.start()
                mSetLeftIn?.start()
                mIsBackVisible = true
                currentFlipState = FlipState.BACK_SIDE
            } else {
                // from back to front
                mSetRightOut?.setTarget(mCardBackLayout)
                mSetLeftIn?.setTarget(mCardFrontLayout)
                mSetRightOut?.start()
                mSetLeftIn?.start()
                mIsBackVisible = false
                currentFlipState = FlipState.FRONT_SIDE
            }
        } else {
            if (mSetTopOut?.isRunning == true || mSetBottomIn?.isRunning == true) return

            mCardBackLayout?.visibility = VISIBLE
            mCardFrontLayout?.visibility = VISIBLE

            if (currentFlipState == FlipState.FRONT_SIDE) {
                // From front to back
                mSetTopOut?.setTarget(mCardFrontLayout)
                mSetBottomIn?.setTarget(mCardBackLayout)
                mSetTopOut?.start()
                mSetBottomIn?.start()
                mIsBackVisible = true
                currentFlipState = FlipState.BACK_SIDE
            } else {
                // from back to front
                mSetTopOut?.setTarget(mCardBackLayout)
                mSetBottomIn?.setTarget(mCardFrontLayout)
                mSetTopOut?.start()
                mSetBottomIn?.start()
                mIsBackVisible = false
                currentFlipState = FlipState.FRONT_SIDE
            }
        }
    }

    /**
     * Flip the view for one side with or without animation.
     *
     * @param withAnimation true means flip view with animation otherwise without animation.
     */
    fun flipTheView(withAnimation: Boolean) {
        if (getChildCount() < 2) return

        if (flipType?.equals("horizontal", ignoreCase = true) == true) {
            if (!withAnimation) {
                mSetLeftIn?.duration = 0
                mSetRightOut?.duration = 0
                val oldFlipEnabled = isFlipEnabled
                isFlipEnabled = true

                flipTheView()

                mSetLeftIn?.duration = flipDuration.toLong()
                mSetRightOut?.duration = flipDuration.toLong()
                isFlipEnabled = oldFlipEnabled
            } else {
                flipTheView()
            }
        } else {
            if (!withAnimation) {
                mSetBottomIn?.duration = 0
                mSetTopOut?.duration = 0
                val oldFlipEnabled = isFlipEnabled
                isFlipEnabled = true

                flipTheView()

                mSetBottomIn?.duration = flipDuration.toLong()
                mSetTopOut?.duration = flipDuration.toLong()
                isFlipEnabled = oldFlipEnabled
            } else {
                flipTheView()
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val index = event.actionIndex
        val action = event.actionMasked
        val pointerId = event.getPointerId(index)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (mVelocityTracker == null) {

                    // Retrieve a new VelocityTracker object to watch the velocity
                    // of a motion.
                    mVelocityTracker = VelocityTracker.obtain()
                } else {

                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker?.clear()
                }

                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.addMovement(event)
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker?.computeCurrentVelocity(1000)

                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                mVelocityTracker?.let {
                    if (VelocityTrackerCompat.getXVelocity(
                            it,
                            pointerId
                        ) > 1500 || VelocityTrackerCompat.getXVelocity(it, pointerId) < -1500
                    ) {
                        flipTheView()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }// Return a VelocityTracker object back to be re-used by others.
        //                mVelocityTracker.recycle();
        return true

    }

    /**
     * Returns duration of flip in milliseconds!
     *
     * @return duration in milliseconds
     */
    fun getFlipDuration(): Int {
        return flipDuration
    }

    /**
     * Sets the flip duration (in milliseconds)
     *
     * @param flipDuration duration in milliseconds
     */
    fun setFlipDuration(flipDuration: Int) {
        this.flipDuration = flipDuration
        if (flipType?.equals("horizontal", ignoreCase = true) == true) {
            //mSetRightOut.setDuration(flipDuration);
            mSetRightOut?.let {
                it.childAnimations[0]?.duration  = flipDuration.toLong()
                it.childAnimations[1].startDelay = (flipDuration / 2).toLong()
            }

            //mSetLeftIn.setDuration(flipDuration);
            mSetLeftIn?.let {
                it.childAnimations[1].duration = flipDuration.toLong()
                it.childAnimations[2].startDelay = (flipDuration / 2).toLong()
            }
        } else {
            mSetTopOut?.let {
                it.childAnimations[0].duration = flipDuration.toLong()
                it.childAnimations[1].startDelay = (flipDuration / 2).toLong()
            }

            mSetBottomIn?.let{
                it.childAnimations[1].duration = flipDuration.toLong()
                it.childAnimations[2].startDelay = (flipDuration / 2).toLong()
            }
        }
    }

    enum class FlipState {
        FRONT_SIDE, BACK_SIDE
    }

    /**
     * The Flip Animation Listener for animations and flipping complete listeners
     */
    interface OnFlipAnimationListener {
        /**
         * Called when flip animation is completed.
         *
         * @param newCurrentSide After animation, the new side of the view. Either can be
         * FlipState.FRONT_SIDE or FlipState.BACK_SIDE
         */
        fun onViewFlipCompleted(easyFlipView: FlipView, newCurrentSide: FlipState)
    }

    companion object {

        val TAG = FlipView::class.java.simpleName

        val DEFAULT_FLIP_DURATION = 400
    }
}