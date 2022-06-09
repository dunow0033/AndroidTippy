package com.express.android.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.express.android.tippy.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //var binding: ActivityMainBinding get() = mBinding!!

//    private lateinit var seekBarTip: SeekBar
//    private lateinit var tvTipPercentLabel: TextView
//    private lateinit var tvTipAmount: TextView
//    private lateinit var tvTotalAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //seekBarTip = findViewById(R.id.seekBarTip)
//        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
//        tvTipAmount = findViewById(R.id.tvTipAmount)
//        tvTotalAmount = findViewById(R.id.tvTotalAmount)

        binding.seekBarTip.progress = INITIAL_TIP_PERCENT
        binding.tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        binding.seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                binding.tvTipPercentLabel.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "onProgressChanged $s")
                computeTipAndTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateTipDescription(tipPercent: Int){
        val tipDescription : String
        when(tipPercent) {
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great"
            else -> tipDescription = "Amazing"
        }
        binding.tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / binding.seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int
        binding.tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(binding.etBaseAmount.text.isEmpty()) {
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text = ""
            return
        }

        val baseAmount = binding.etBaseAmount.text.toString().toDouble()
        val tipPercent = binding.seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        binding.tvTipAmount.text = "%.2f".format(tipAmount)
        binding.tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}