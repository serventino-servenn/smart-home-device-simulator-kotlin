import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice(val name: String, val category: String) {

    var deviceStatus = "off"
        protected set

    open val deviceType = "unknown"

    open fun turnOn() {
        deviceStatus = "on"
    }

    open fun turnOff() {
        deviceStatus = "off"
    }
    fun printDeviceInfo(){
        println("Device name: $name,category: $category,type:$deviceType,status:$deviceStatus")
    }
}

class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart TV"

    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)

    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }
    fun decreaseVolume(){
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume")
    }
    fun previousChannel(){
        channelNumber--
        println("Channel number decreased to $channelNumber")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increased to $channelNumber.")
    }

    override fun turnOn() {
        super.turnOn()
        println(
            "$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                    "set to $channelNumber."
        )
    }

    override fun turnOff() {
        super.turnOff()
        println("$name turned off")
    }
}

class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart Light"

    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }
    fun decreaseBrightness(){
        brightnessLevel--
        println("Brightness decreased to $brightnessLevel")
    }

    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }
}

class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {

    var deviceTurnOnCount = 0
        private set

    fun turnOnTv() {
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }

    fun turnOffTv() {
        deviceTurnOnCount--
        smartTvDevice.turnOff()
    }

    fun increaseTvVolume() {
        if(smartTvDevice.deviceStatus == "on"){
            smartTvDevice.increaseSpeakerVolume()
        }else{
            println("Attempt to increase tv volume failed because tv is off")
        }
    }
    fun decreaseTvVolume(){
        if(smartTvDevice.deviceStatus == "on"){
            smartTvDevice.decreaseVolume()
        }else{
            println("Attempt to decrease tv volume failed because tv is off")
        }
    }
    fun changeTvChannelToNext() {
        if(smartTvDevice.deviceStatus == "on"){
            smartTvDevice.nextChannel()
        }else{
            println("Attempt to change tv channel failed because tv is off")
        }
    }
    fun changeTvChannelToPrevious(){
        if(smartTvDevice.deviceStatus == "on"){
            smartTvDevice.previousChannel()
        }else{
            println("Attempt to change tv channel failed because tv is off")
        }
    }
    fun printSmartTvInfo(){
            smartTvDevice.printDeviceInfo()
            println()
    }

    fun turnOnLight() {
        deviceTurnOnCount++
        smartLightDevice.turnOn()
    }

    fun turnOffLight() {
        deviceTurnOnCount--
        smartLightDevice.turnOff()
    }

    fun increaseLightBrightness() {
        if(smartLightDevice.deviceStatus == "on") {
            smartLightDevice.increaseBrightness()
        }else{
            println("Failed to increase brightness because light is off")
        }
    }
    fun decreaseLightBrightness(){
        if(smartLightDevice.deviceStatus == "on") {
            smartLightDevice.decreaseBrightness()
        }else{
            println("Failed to decrease brightness because light is off")
        }
    }
    fun printSmartLightInfo(){
        smartLightDevice.printDeviceInfo()
    }

    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun main() {
    val smartTvDevice: SmartTvDevice = SmartTvDevice("Android TV", "Entertainment")

    val smartLightDevice:SmartLightDevice = SmartLightDevice("Google Light", "Utility")

     val smartHome = SmartHome(smartTvDevice = smartTvDevice, smartLightDevice = smartLightDevice)

    //Perform actions when device status is off
    //tv
    smartHome.decreaseTvVolume()
    smartHome.changeTvChannelToPrevious()
    smartHome.printSmartTvInfo()
    //smart light
    smartHome.decreaseLightBrightness()
    smartHome.printSmartLightInfo()

    //Perform actions when device status is on
    //tv
    smartHome.turnOnTv()
    smartHome.increaseTvVolume()
    smartHome.changeTvChannelToNext()
    smartHome.printSmartTvInfo()
    //smart light
    smartHome.turnOnLight()
    smartHome.increaseLightBrightness()

    //combine device functions
    //tv
    smartHome.turnOnTv()
    smartHome.increaseTvVolume()
    smartHome.turnOffTv()
    smartHome.increaseTvVolume()
    //smart light
    smartHome.turnOnLight()
    smartHome.increaseLightBrightness()
    smartHome.turnOffLight()
    smartHome.decreaseLightBrightness()


}
