import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

public class CoffeeCleanBean implements Writable {
    int hour_of_day;          // 对应tinyint类型
    String cash_type;         // 对应varchar(10)
    BigDecimal money;         // 对应decimal(6,1)
    String coffee_name;       // 对应varchar(50)
    String Time_of_Day;       // 对应varchar(20)
    String Weekday;           // 对应varchar(10)
    String Month_name;        // 对应varchar(10)
    int Weekdaysort;          // 对应tinyint
    int Monthsort;            // 对应tinyint
    Date Date;                // 对应date类型
    String Time;              // 对应varchar(20)

    public CoffeeCleanBean() {
    }

    public int getHour_of_day() {
        return hour_of_day;
    }

    public void setHour_of_day(int hour_of_day) {
        this.hour_of_day = hour_of_day;
    }

    public String getCash_type() {
        return cash_type;
    }

    public void setCash_type(String cash_type) {
        this.cash_type = cash_type;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCoffee_name() {
        return coffee_name;
    }

    public void setCoffee_name(String coffee_name) {
        this.coffee_name = coffee_name;
    }

    public String getTime_of_Day() {
        return Time_of_Day;
    }

    public void setTime_of_Day(String time_of_Day) {
        Time_of_Day = time_of_Day;
    }

    public String getWeekday() {
        return Weekday;
    }

    public void setWeekday(String weekday) {
        Weekday = weekday;
    }

    public String getMonth_name() {
        return Month_name;
    }

    public void setMonth_name(String month_name) {
        Month_name = month_name;
    }

    public int getWeekdaysort() {
        return Weekdaysort;
    }

    public void setWeekdaysort(int weekdaysort) {
        Weekdaysort = weekdaysort;
    }

    public int getMonthsort() {
        return Monthsort;
    }

    public void setMonthsort(int monthsort) {
        Monthsort = monthsort;
    }

    public java.sql.Date getDate() {
        return Date;
    }

    public void setDate(java.sql.Date date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(hour_of_day);
        output.writeUTF(cash_type);
        output.writeUTF(money.toString());  // BigDecimal以字符串形式存储
        output.writeUTF(coffee_name);
        output.writeUTF(Time_of_Day);
        output.writeUTF(Weekday);
        output.writeUTF(Month_name);
        output.writeInt(Weekdaysort);
        output.writeInt(Monthsort);
        output.writeUTF(Date.toString());   // Date以字符串形式存储
        output.writeUTF(Time);

    }

    @Override
    public void readFields(DataInput input) throws IOException {
        this.hour_of_day = input.readInt();
        this.cash_type = input.readUTF();
        this.money = new BigDecimal(input.readUTF());
        this.coffee_name = input.readUTF();
        this.Time_of_Day = input.readUTF();
        this.Weekday = input.readUTF();
        this.Month_name = input.readUTF();
        this.Weekdaysort = input.readInt();
        this.Monthsort = input.readInt();
        this.Date = Date.valueOf(input.readUTF());  // 解析为Date类型
        this.Time = input.readUTF();
    }

    // 重写toString，用于输出清洗后的数据
    @Override
    public String toString() {
        return hour_of_day + "," +
                cash_type + "," +
                money + "," +
                coffee_name + "," +
                Time_of_Day + "," +
                Weekday + "," +
                Month_name + "," +
                Weekdaysort + "," +
                Monthsort + "," +
                Date + "," +
                Time;
    }
}
