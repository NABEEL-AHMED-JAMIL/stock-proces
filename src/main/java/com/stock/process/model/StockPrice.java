package com.stock.process.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Nabeel Ahmed
 */
@Entity
@Table(name = "stock_price")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockPrice {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "open", nullable = false)
    private Double open = 0.0;

    @Column(name = "high", nullable = false)
    private Double high = 0.0;

    @Column(name = "low", nullable = false)
    private Double low = 0.0;

    @Column(name = "close", nullable = false)
    private Double close = 0.0;

    @Column(name = "volume", nullable = false)
    private Double volume = 0.0;

    @Column(name = "open_int", nullable = false)
    private Double openInt = 0.0;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo; // each file process reference

    public StockPrice() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getOpenInt() {
        return openInt;
    }

    public void setOpenInt(Double openInt) {
        this.openInt = openInt;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
